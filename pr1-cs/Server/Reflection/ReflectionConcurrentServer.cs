using System.Net.Sockets;
using System.Threading;
using Networking.Reflection;
using Services.Reflection;

namespace Server.Reflection
{
    public class ReflectionConcurrentServer : ConcurrentServer
    {
        private readonly IServices _services;
        
        public ReflectionConcurrentServer(string host, int port, IServices services) : base(host, port)
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