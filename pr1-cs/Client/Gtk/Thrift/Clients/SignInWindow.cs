using System;
using System.Configuration;
using System.Threading.Tasks;
using Gtk;
using Model;
using Services.Thrift;
using Services.Thrift.DataTransfer;
using Thrift.Transport;

namespace Client.Gtk.Thrift.Clients
{
    public class SignInWindow : Window
    {
        public override Window Init(TTransport connection, Services.Thrift.Services.Client services, User signedInUser)
        {
            base.Init(connection, services, signedInUser);

            GuiElements.AddFromFile(ConfigurationManager.AppSettings["signInWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (global::Gtk.Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate { Close(); };

            return this;
        }

        public async void SignIn(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GuiElements.GetObject("UsernameEntry")).Text;
                var password = ((Entry) GuiElements.GetObject("PasswordEntry")).Text;
                var mainClient = new MainWindow();
                mainClient.Init(Connection, Services, DtoUtils.ToUser(await Services.signInUser(username, password))).Open();
                Close();
            }
            catch (Exception exception)
            {
                var dialog = new MessageDialog(OwnedWindow,
                    DialogFlags.DestroyWithParent, MessageType.Error,
                    ButtonsType.Close, exception.Message);
                dialog.Run();
                dialog.Destroy();
            }
        }

        public void ToSignUp(object sender, EventArgs args)
        {
            new SignUpWindow().Init(Connection, Services, null).Open();
            Close();
        }
    }
}