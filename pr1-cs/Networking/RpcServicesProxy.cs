using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using System.Threading.Tasks;
using Model;
using Model.Exceptions;
using Model.Observers;
using Networking.DataTransfer;
using Networking.RpcProtocol;
using Services;

namespace Networking
{
    public class RpcServicesProxy : IServices
    {
        private readonly string _host;
        private readonly int _port;
        private readonly Queue<Response> _responses;
        private IObserver _client;
        private TcpClient _connection;
        private volatile bool _finished;
        private IFormatter _formatter;
        private NetworkStream _stream;
        private EventWaitHandle _waitHandle;

        public RpcServicesProxy(string host, int port)
        {
            _host = host;
            _port = port;
            _responses = new Queue<Response>();
        }

        public User SignInUser(string username, string password, IObserver client)
        {
            var user = new User(username, password);
            var userDto = DtoUtils.ToDto(user);

            var request = new Request(RequestType.SignIn, userDto);
            SendRequest(request);

            var response = ReadResponse();
            switch (response.Type)
            {
                case ResponseType.Ok:
                    _client = client;
                    user = DtoUtils.ToUser((UserDto) response.Data);
                    return user;
                case ResponseType.Error:
                    throw new Exception(response.Data.ToString());
                default:
                    throw new NetworkingException("received wrong response " + response.Type);
            }
        }

        public void SignOutUser(string username, IObserver client)
        {
            var usernameDto = DtoUtils.ToDto(username);

            var request = new Request(RequestType.SignOut, usernameDto);
            SendRequest(request);

            var response = ReadResponse();
            CloseConnection();
            if (response.Type == ResponseType.Error) throw new Exception(response.Data.ToString());
        }

        public User SignUpUser(string username, string password, IObserver client)
        {
            var user = new User(username, password);
            var userDto = DtoUtils.ToDto(user);

            var request = new Request(RequestType.SignUp, userDto);
            SendRequest(request);

            var response = ReadResponse();
            switch (response.Type)
            {
                case ResponseType.Ok:
                    _client = client;
                    user = DtoUtils.ToUser((UserDto) response.Data);

                    return user;
                case ResponseType.Error:
                    throw new Exception(response.Data.ToString());
                default:
                    throw new NetworkingException("received wrong response " + response.Type);
            }
        }

        public IEnumerable<Game> GetAllGames()
        {
            var request = new Request(RequestType.GetAllGames);
            SendRequest(request);

            var response = ReadResponse();
            switch (response.Type)
            {
                case ResponseType.Ok:
                {
                    var games = DtoUtils.ToGameCollection((GameCollectionDto) response.Data);

                    return games;
                }
                case ResponseType.Error:
                    throw new Exception(response.Data.ToString());
                default:
                    throw new NetworkingException("received wrong response " + response.Type);
            }
        }

        public void SellSeats(Game game, string clientName, int seatsCount)
        {
            var seatsSellingDto = DtoUtils.ToDto(game, clientName, seatsCount);

            var request = new Request(RequestType.SellSeats, seatsSellingDto);
            SendRequest(request);

            var response = ReadResponse();
            if (response.Type == ResponseType.Error) throw new Exception(response.Data.ToString());
        }

        public IEnumerable<Game> GetGamesWithAvailableSeatsDescending()
        {
            var request = new Request(RequestType.GetGamesWithAvailableSeatsDescending);
            SendRequest(request);

            var response = ReadResponse();
            switch (response.Type)
            {
                case ResponseType.Ok:
                {
                    var games = DtoUtils.ToGameCollection((GameCollectionDto) response.Data);

                    return games;
                }
                case ResponseType.Error:
                    throw new Exception(response.Data.ToString());
                default:
                    throw new NetworkingException("received wrong response " + response.Type);
            }
        }

        private void EnsureConnection()
        {
            if (_connection != null && _stream != null && _formatter != null)
                return;

            try
            {
                _connection = new TcpClient(_host, _port);
                _stream = _connection.GetStream();
                _formatter = new BinaryFormatter();
                _finished = false;
                _waitHandle = new AutoResetEvent(false);
                var tw = new Thread(Run);
                tw.Start();
            }
            catch (Exception)
            {
                throw new NetworkingException("could not ensure connection");
            }
        }

        private void CloseConnection()
        {
            _finished = true;
            try
            {
                _stream.Close();
                _connection.Close();
                _waitHandle.Close();
                _client = null;
            }
            catch (Exception)
            {
                throw new NetworkingException("could not close connection");
            }
        }

        private void SendRequest(Request request)
        {
            EnsureConnection();
            try
            {
                _formatter.Serialize(_stream, request);
                _stream.Flush();
            }
            catch (Exception exception)
            {
                throw new NetworkingException("error sending request " + exception);
            }
        }

        private Response ReadResponse()
        {
            EnsureConnection();
            try
            {
                _waitHandle.WaitOne();
                Response response;
                lock (_responses)
                {
                    response = _responses.Dequeue();
                }

                return response;
            }
            catch (Exception)
            {
                throw new NetworkingException("could not read response");
            }
        }

        private void Run()
        {
            while (!_finished)
                try
                {
                    EnsureConnection();
                    var response = (Response) _formatter.Deserialize(_stream);
                    HandleResponse(response);
                }
                catch (Exception exception)
                {
                    Console.Out.WriteLine(exception);
                }
        }

        private void HandleResponse(Response response)
        {
            if (response.Type == ResponseType.Ok ||
                response.Type == ResponseType.Error)
            {
                lock (_responses)
                {
                    _responses.Enqueue(response);
                }

                _waitHandle.Set();

                return;
            }

            var handlerName = "Handle_" + response.Type;
            try
            {
                Task.Run(() => { GetType().GetMethod(handlerName)?.Invoke(this, new object[] {response}); });
            }
            catch (Exception)
            {
                throw new NetworkingException("unknown response");
            }
        }

        public void Handle_SeatsSold(Response response)
        {
            var seatsSoldDto = (SeatsSoldDto) response.Data;
            _client.SeatsSold(seatsSoldDto.GameId, seatsSoldDto.SeatsCount);
        }
    }
}