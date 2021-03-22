using System;
using pr1_cs.Domain;
using pr1_cs.Repository;
using pr1_cs.Repository.Database;
using pr1_cs.UserInterfacing;

namespace pr1_cs
{
    internal static class Program
    {
        private static void Main(string[] args)
        {
	        GtkClient client = new GtkClient();
	        client.Run(args);

	        // IUserRepository userRepository = new UserDbRepository();
	        // IGameRepository gameRepository = new GameDbRepository();
	        // ITicketRepository ticketRepository = new TicketDbRepository();
	        //          userRepository.Add(new User(1, "adipopbv", "logged-out"));
	        //          gameRepository.Add(new Game(1, "Semifinals", "Rockets", "Celtics", 300, 200));
	        //          ticketRepository.Add(new Ticket(1, gameRepository.GetOne(1), "Pop Adrian"));
	        // Console.Out.WriteLine("All users: " + userRepository);
	        // Console.Out.WriteLine("All games: " + gameRepository);
	        // Console.Out.WriteLine("All tickets: " + ticketRepository);
	        //          userRepository.Modify(1, new User(1, "adipopbv", "logged-in"));
	        //          gameRepository.Modify(1, new Game(1, "Finals", "Celtics", "Rockets", 450, 150));
	        //          ticketRepository.Modify(1, new Ticket(1, gameRepository.GetOne(1), "Adrian Pop"));
	        // Console.Out.WriteLine("All users: " + userRepository);
	        // Console.Out.WriteLine("All games: " + gameRepository);
	        // Console.Out.WriteLine("All tickets: " + ticketRepository);
	        //          userRepository.Remove(1);
	        //          gameRepository.Remove(1);
	        //          // ticketRepository.Remove(1);
	        // Console.Out.WriteLine("All users: " + userRepository);
	        // Console.Out.WriteLine("All games: " + gameRepository);
	        // Console.Out.WriteLine("All tickets: " + ticketRepository);
        }
    }
}