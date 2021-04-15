using Gtk;
using Model;
using Services;
using Services.Reflection;

namespace Client.Gtk.Reflection.Clients
{
    public abstract class Window
    {
        protected readonly Builder GuiElements = new Builder();
        protected IServices Services;
        protected User SignedInUser;
        protected global::Gtk.Window OwnedWindow;

        public virtual Window Init(IServices services, User signedInUser)
        {
            ClientStarter.OpenWindows++;
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
            ClientStarter.OpenWindows--;
            if (ClientStarter.OpenWindows <= 0)
                Application.Quit();
            OwnedWindow.Destroy();
        }
    }
}