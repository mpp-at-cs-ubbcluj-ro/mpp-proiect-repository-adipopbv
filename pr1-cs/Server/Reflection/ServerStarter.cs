using System;
using System.Configuration;
using Persistence;
using Persistence.Database;
using Services.Reflection;
using ServerException = Model.Exceptions.Thrift.ServerException;

namespace Server.Reflection
{
    public class ServerStarter
    {
        private const string DefaultHost = "127.0.0.1";
        private const string DefaultPort = "55555";

        public void Run(string[] args)
        {
            IUserRepository userRepository = new UserDbRepository();
            IGameRepository gameRepository = new GameDbRepository();
            ITicketRepository ticketRepository = new TicketDbRepository();
            IServices services = new Services.Reflection.Handler(userRepository, gameRepository, ticketRepository);
            
            Server server =
                new ReflectionConcurrentServer(ConfigurationManager.AppSettings["serverHost"] ?? DefaultHost,
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