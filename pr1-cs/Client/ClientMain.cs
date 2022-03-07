namespace Client
{
    internal static class ClientMain
    {
        public static void Main(string[] args)
        {
            new RestClientStarter().Run(args);
        }
    }
}