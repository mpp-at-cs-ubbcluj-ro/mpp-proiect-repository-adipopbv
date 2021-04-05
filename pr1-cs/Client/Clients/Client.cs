using Gtk;
using Model;
using Services;

namespace Client.Clients
{
    public abstract class Client
    {
        protected readonly Builder GuiElements = new Builder();
        protected IServices Services;
        protected User SignedInUser;
        protected Window OwnedWindow;

        public virtual Client Init(IServices services, User signedInUser)
        {
            GtkClientStarter.OpenWindows++;
            Services = services;
            SignedInUser = signedInUser;

            return this;
        }

        public void Open()
        {
            OwnedWindow.Show();
        }

        protected void Close()
        {
            GtkClientStarter.OpenWindows--;
            if (GtkClientStarter.OpenWindows <= 0)
                Application.Quit();
            OwnedWindow.Destroy();
        }
    }
}