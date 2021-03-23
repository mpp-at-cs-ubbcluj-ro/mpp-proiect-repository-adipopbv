using System.Collections.Generic;
using System.Configuration;
using Gtk;
using pr1_cs.Repository.Database;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class GtkClient
    {
        public static int OpenWindows = 0;

        public void Run(string[] args)
        {
            Application.Init();
            new LogInController(new Service(new UserDbRepository(), new GameDbRepository(), new TicketDbRepository()), null).Open();
            Application.Run();
        }
    }
}