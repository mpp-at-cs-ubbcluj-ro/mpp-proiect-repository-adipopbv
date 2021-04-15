using Gtk;
using Model;
using Services;
using Services.Reflection;
using Services.Thrift;
using Thrift.Transport;

namespace Client.Gtk.Thrift.Clients
{
    public abstract class Window
    {
        protected TTransport Connection;
        protected ThriftServices.Client Services;
        protected readonly Builder GuiElements = new Builder();
        protected User SignedInUser;
        protected global::Gtk.Window OwnedWindow;

        public virtual Window Init(TTransport connection, ThriftServices.Client services, User signedInUser)
        {
            ClientStarter.OpenWindows++;
            Connection = connection;
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
            {
                Connection.Close();
                Application.Quit();
            }
            OwnedWindow.Destroy();
        }
    }
}