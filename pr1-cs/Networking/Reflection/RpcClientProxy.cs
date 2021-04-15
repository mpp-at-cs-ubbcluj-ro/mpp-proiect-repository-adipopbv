using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using Model;
using Model.Exceptions;
using Model.Observers;
using Networking.Reflection.DataTransfer;
using Services.Reflection;

namespace Networking.Reflection
{
    public class RpcClientProxy : IObserver
    {
        private readonly TcpClient _connection;
        private readonly IServices _services;
        private volatile bool _connected;
        private IFormatter _formatter;
        private NetworkStream _stream;

        public RpcClientProxy(IServices services, TcpClient connection)
        {
            _services = services;
            _connection = connection;
        }

        public void SeatsSold(int gameId, int seatsCount)
        {
            try
            {
                var seatsSoldDto = DtoUtils.ToDto(gameId, seatsCount);
                var response = new Response(ResponseType.SeatsSold, seatsSoldDto);
                SendResponse(response);
            }
            catch (Exception)
            {
                Console.Out.WriteLine("could not send seats sold to client");
            }
        }

        private void EnsureConnection()
        {
            if (_stream != null && _formatter != null)
                return;
            if (_connection == null)
                throw new NetworkingException("lost connection to client");

            try
            {
                _stream = _connection.GetStream();
                _formatter = new BinaryFormatter();
                _connected = true;
            }
            catch (Exception)
            {
                throw new NetworkingException("could not ensure connection");
            }
        }

        private void CloseConnection()
        {
            try
            {
                _stream.Close();
                _connection.Close();
            }
            catch (Exception)
            {
                Console.Out.WriteLine("could not close connection");
            }
        }

        private void SendResponse(Response response)
        {
            EnsureConnection();
            try
            {
                _formatter.Serialize(_stream, response);
                _stream.Flush();
            }
            catch (Exception exception)
            {
                throw new NetworkingException("error sending response " + exception);
            }
        }

        public void Run()
        {
            try
            {
                EnsureConnection();
                while (_connected)
                    try
                    {
                        EnsureConnection();
                        var request = (Request) _formatter.Deserialize(_stream);
                        var response = HandleRequest(request);
                        if (response != null)
                            SendResponse(response);
                    }
                    catch (Exception exception)
                    {
                        Console.Out.WriteLine(exception);
                    }

                CloseConnection();
            }
            catch (Exception exception)
            {
                Console.Out.WriteLine(exception);
            }
        }

        private Response HandleRequest(Request request)
        {
            var handlerName = "Handle_" + request.Type;
            try
            {
                var methodInfo = GetType().GetMethod(handlerName);
                return (Response) methodInfo?.Invoke(this, new object[] {request});
            }
            catch (Exception exception)
            {
                return new Response(ResponseType.Error, "unknown request");
            }
        }

        public Response Handle_SignIn(Request request)
        {
            Response response;
            try
            {
                var user = DtoUtils.ToUser((UserDto) request.Data);
                _services.SignInUser(user.Username, user.Password, this);
                response = new Response(ResponseType.Ok, request.Data);
            }
            catch (Exception exception)
            {
                response = new Response(ResponseType.Error, exception.Message);
            }

            return response;
        }

        public Response Handle_SignOut(Request request)
        {
            Response response;
            try
            {
                _services.SignOutUser(DtoUtils.ToUsername((UsernameDto) request.Data), this);
                _connected = false;
                response = new Response(ResponseType.Ok);
            }
            catch (Exception exception)
            {
                response = new Response(ResponseType.Error, exception.Message);
            }

            return response;
        }

        public Response Handle_SignUp(Request request)
        {
            Response response;
            try
            {
                var user = DtoUtils.ToUser((UserDto) request.Data);
                _services.SignUpUser(user.Username, user.Password, this);
                response = new Response(ResponseType.Ok, request.Data);
            }
            catch (Exception exception)
            {
                response = new Response(ResponseType.Error, exception.Message);
            }

            return response;
        }

        public Response Handle_GetAllGames(Request request)
        {
            Response response;
            try
            {
                var gamesCollection = new Collection<Game>(_services.GetAllGames().ToList());
                var gameCollectionDto = DtoUtils.ToDto(gamesCollection);
                response = new Response(ResponseType.Ok, gameCollectionDto);
            }
            catch (Exception exception)
            {
                response = new Response(ResponseType.Error, exception.Message);
            }

            return response;
        }

        public Response Handle_SellSeats(Request request)
        {
            Response response;
            try
            {
                var seatsSellingDto = (SeatsSellingDto) request.Data;
                var game = DtoUtils.ToGame(seatsSellingDto.GameDto);
                _services.SellSeats(game, seatsSellingDto.ClientName, seatsSellingDto.SeatsCount);
                response = new Response(ResponseType.Ok);
            }
            catch (Exception exception)
            {
                response = new Response(ResponseType.Error, exception.Message);
            }

            return response;
        }

        public Response Handle_GetGamesWithAvailableSeatsDescending(Request request)
        {
            Response response;
            try
            {
                var gamesCollection = new Collection<Game>(_services.GetGamesWithAvailableSeatsDescending().ToList());
                var gameCollectionDto = DtoUtils.ToDto(gamesCollection);
                response = new Response(ResponseType.Ok, gameCollectionDto);
            }
            catch (Exception exception)
            {
                response = new Response(ResponseType.Error, exception.Message);
            }

            return response;
        }
    }
}