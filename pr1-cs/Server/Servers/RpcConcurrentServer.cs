using System.Net.Sockets;
using System.Threading;
using Networking;
using Services;

namespace Server.Servers
{
    public class RpcConcurrentServer : ConcurrentServer
    {
        private readonly IServices _services;
        
        public RpcConcurrentServer(string host, int port, IServices services) : base(host, port)
        {
            _services = services;
        }

        protected override Thread CreateClientProxyThread(TcpClient connection)
        {
            var clientProxy = new RpcClientProxy(_services, connection);
            return new Thread(clientProxy.Run);
        }
    }
}