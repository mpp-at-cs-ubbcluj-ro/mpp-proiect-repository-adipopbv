using System;
using System.Configuration;
using Gtk;
using Model;
using Services;
using Services.Reflection;

namespace Client.Gtk.Reflection.Clients
{
    public class SignInWindow : Window
    {
        public override Window Init(IServices services, User signedInUser)
        {
            base.Init(services, signedInUser);

            GuiElements.AddFromFile(ConfigurationManager.AppSettings["signInWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (global::Gtk.Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate { Close(); };

            return this;
        }

        public void SignIn(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GuiElements.GetObject("UsernameEntry")).Text;
                var password = ((Entry) GuiElements.GetObject("PasswordEntry")).Text;
                var mainClient = new MainWindow();
                mainClient.Init(Services, Services.SignInUser(username, password, mainClient)).Open();
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
            new SignUpWindow().Init(Services, null).Open();
            Close();
        }
    }
}