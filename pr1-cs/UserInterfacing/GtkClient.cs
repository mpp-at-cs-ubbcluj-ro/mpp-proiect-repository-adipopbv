using System.Collections.Generic;
using System.Configuration;
using Gtk;
using pr1_cs.Repository.Database;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class GtkClient
    {
        public static readonly Builder ClientElements = new Builder();
        public static int OpenWindows = 0;

        public GtkClient()
        {
            Application.Init();
            ClientElements.AddFromFile(ConfigurationManager.AppSettings["gtkGuiSourceFile"]);
            ClientElements.Autoconnect(this);
        }

        public void Run(string[] args)
        {
            Application.Run();
            new LogInController(new Service(new UserDbRepository(), new GameDbRepository(), new TicketDbRepository()), null).Open();
        }
    }
}