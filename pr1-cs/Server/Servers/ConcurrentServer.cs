using System.Net.Sockets;
using System.Threading;

namespace Server.Servers
{
    public abstract class ConcurrentServer : Server
    {
        protected ConcurrentServer(string host, int port) : base(host, port)
        {
            
        }

        protected override void BeginConversation(TcpClient connection)
        {
            Thread clientProxyThread = CreateClientProxyThread(connection);
            clientProxyThread.Start();
        }

        protected abstract Thread CreateClientProxyThread(TcpClient connection);
    }
}