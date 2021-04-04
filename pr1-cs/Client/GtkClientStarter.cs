using System.Configuration;
using Gtk;
using Client.Clients;
using Services;
using Networking;

namespace Client
{
    public class GtkClientStarter
    {
        private readonly string _defaultHost = "localhost";
        private readonly string _defaultPort = "55555";

        public static int OpenWindows = 0;
        
        public void Run(string[] args)
        {
            Application.Init();
            IServices services = new ServicesProxy(ConfigurationManager.AppSettings["serverHost"] ?? _defaultHost,
                int.Parse(ConfigurationManager.AppSettings["serverPost"] ?? _defaultPort));
            new SignInClient().Init(services, null).Open();
            Application.Run();
        }
    }
}