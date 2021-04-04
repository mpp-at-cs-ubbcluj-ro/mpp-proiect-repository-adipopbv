using System;

namespace Client
{
    internal static class ClientMain
    {
        private static void Main(string[] args)
        {
            new GtkClientStarter().Run(args);
        }
    }
}