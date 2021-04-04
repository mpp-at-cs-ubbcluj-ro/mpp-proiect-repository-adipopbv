using System;
using System.Configuration;
using Gtk;
using Model;
using Services;

namespace Client.Clients
{
    public class SignUpClient : Client
    {
        public override Client Init(IServices services, User signedInUser)
        {
            base.Init(services, signedInUser);
            
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["signUpWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate
            {
                Close();
            };

            return this;
        }

        public void SignUp(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GuiElements.GetObject("UsernameEntry")).Text;
                var password = ((Entry) GuiElements.GetObject("PasswordEntry")).Text;
                var mainClient = new MainClient();
                mainClient.Init(Services, Services.SignUpUser(username, password, mainClient)).Open();
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

        public void ToSignIn(object sender, EventArgs args)
        {
            new SignInClient().Init(Services, null).Open();
            Close();
        }
    }
}