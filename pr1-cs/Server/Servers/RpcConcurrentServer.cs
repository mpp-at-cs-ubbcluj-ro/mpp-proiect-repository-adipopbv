using System.Net.Sockets;
using System.Threading;
using Networking;
using Services;
using Services.Reflection;

namespace Server.Servers
{
    public class RpcConcurrentServer : ConcurrentServer
    {
        private readonly IReflectionServices _services;
        
        public RpcConcurrentServer(string host, int port, IReflectionServices services) : base(host, port)
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