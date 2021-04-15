using System.Configuration;
using System.Net;
using Client.Gtk.Thrift.Clients;
using Gtk;
using Services.Thrift;
using Thrift;
using Thrift.Protocol;
using Thrift.Transport;
using Thrift.Transport.Client;

namespace Client.Gtk.Thrift
{
    public class ClientStarter
    {
        private const string DefaultHost = "127.0.0.1";
        private const string DefaultPort = "55555";

        public static int OpenWindows = 0;

        public void Run(string[] args)
        {
            Application.Init();
            TTransport connection = new TSocketTransport(IPAddress.Parse(ConfigurationManager.AppSettings["serverHost"] ?? DefaultHost),
                int.Parse(ConfigurationManager.AppSettings["serverPost"] ?? DefaultPort), new TConfiguration());
            connection.OpenAsync();
            var services = new ThriftServices.Client(new TBinaryProtocol(connection));
            
            new SignInWindow().Init(connection, services, null).Open();
            Application.Run();
        }
    }
}