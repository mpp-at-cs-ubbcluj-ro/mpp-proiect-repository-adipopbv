using System;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public abstract class GuiController
    {
        protected readonly Builder GuiElements = new Builder();
        protected readonly Service Service;
        protected User LoggedUser;
        protected Window OwnedWindow;

        protected GuiController(Service service, User loggedUser)
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
            GtkClient.OpenWindows--;
            if (GtkClient.OpenWindows <= 0)
                Application.Quit();
            OwnedWindow.Destroy();
        }
    }
}