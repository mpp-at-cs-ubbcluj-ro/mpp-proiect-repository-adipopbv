using System.Configuration;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public abstract class UserInterface
    {
        protected readonly Service Service;
        protected User LoggedUser;
        protected Window OwnedWindow;

        protected UserInterface(Service service, User loggedUser)
        {
            GtkClient.OpenWindows++;
            Service = service;
            LoggedUser = loggedUser;
        }

        public void Open()
        {
            OwnedWindow.Show();
        }

        protected void Close()
        {
            OwnedWindow.Destroy();
        }
    }
}