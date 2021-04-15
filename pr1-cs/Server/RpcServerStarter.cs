using System;
using System.Configuration;
using Model.Exceptions;
using Persistence;
using Persistence.Database;
using Server.Servers;
using Services;
using Services.Reflection;

namespace Server
{
    public class RpcServerStarter
    {
        private const string DefaultHost = "127.0.0.1";
        private const string DefaultPort = "55555";

        public void Run(string[] args)
        {
            IUserRepository userRepository = new UserDbRepository();
            IGameRepository gameRepository = new GameDbRepository();
            ITicketRepository ticketRepository = new TicketDbRepository();
            IReflectionServices services = new Services.Reflection.ReflectionHandler(userRepository, gameRepository, ticketRepository);
            
            Servers.Server server =
                new RpcConcurrentServer(ConfigurationManager.AppSettings["serverHost"] ?? DefaultHost,
                    int.Parse(ConfigurationManager.AppSettings["serverPost"] ?? DefaultPort),
                    services);
            try {
                server.Start();
            } catch (ServerException exception) {
                Console.Out.WriteLine("could not start the server", exception);
            } finally {
                try {
                    server.Stop();
                } catch (ServerException exception) {
                    Console.Out.WriteLine("could not stop the server", exception);
                }
            }

        }
    }
}