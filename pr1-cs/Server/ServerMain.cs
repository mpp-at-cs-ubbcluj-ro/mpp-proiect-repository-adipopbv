namespace Server
{
    internal static class ServerMain
    {
        private static void Main(string[] args)
        {
            new RpcServerStarter().Run(args);
        }
    }
}