using Server.Reflection;

namespace Server
{
    internal static class ServerMain
    {
        private static void Main(string[] args)
        {
            new ServerStarter().Run(args);
        }
    }
}