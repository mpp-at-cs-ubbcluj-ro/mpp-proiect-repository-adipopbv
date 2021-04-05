using System.Configuration;
using Gtk;
using Client.Clients;
using Services;
using Networking;

namespace Client
{
    public class GtkClientStarter
    {
        private const string DefaultHost = "127.0.0.1";
        private const string DefaultPort = "55555";

        public static int OpenWindows = 0;
        
        public void Run(string[] args)
        {
            Application.Init();
            IServices services = new RpcServicesProxy(ConfigurationManager.AppSettings["serverHost"] ?? DefaultHost,
                int.Parse(ConfigurationManager.AppSettings["serverPost"] ?? DefaultPort));
            new SignInClient().Init(services, null).Open();
            Application.Run();
        }
    }
}