using System.Configuration;
using Client.Gtk.Reflection.Clients;
using Gtk;
using Networking;
using Services;
using Services.Reflection;

namespace Client.Gtk.Reflection
{
    public class ClientStarter
    {
        private const string DefaultHost = "127.0.0.1";
        private const string DefaultPort = "55555";

        public static int OpenWindows = 0;
        
        public void Run(string[] args)
        {
            Application.Init();
            IReflectionServices services = new RpcServicesProxy(ConfigurationManager.AppSettings["serverHost"] ?? DefaultHost,
                int.Parse(ConfigurationManager.AppSettings["serverPost"] ?? DefaultPort));
            new SignInWindow().Init(services, null).Open();
            Application.Run();
        }
    }
}