using Client.Gtk.Thrift;

namespace Client
{
    internal static class ClientMain
    {
        public static void Main(string[] args)
        {
            new ClientStarter().Run(args);
        }
    }
}