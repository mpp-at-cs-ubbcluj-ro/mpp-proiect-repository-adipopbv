using System;
using System.Net;
using System.Net.Sockets;
using Model.Exceptions;

namespace Server
{
    public abstract class Server
    {
        private readonly string _host;
        private readonly int _port;
        private TcpListener _serverListener;

        protected Server(string host, int port)
        {
            _host = host;
            _port = port;
        }

        public void Start()
        {
            _serverListener = new TcpListener(new IPEndPoint(IPAddress.Parse(_host), _port));
            _serverListener.Start();
            try
            {
                while (true)
                {
                    var client = _serverListener.AcceptTcpClient();
                    BeginConversation(client);
                }
            }
            catch (Exception exception)
            {
                throw new ServerException("error trying to connect to client");
            }
            finally
            {
                Stop();
            }
        }

        public void Stop()
        {
        }

        protected abstract void BeginConversation(TcpClient connection);
    }
}