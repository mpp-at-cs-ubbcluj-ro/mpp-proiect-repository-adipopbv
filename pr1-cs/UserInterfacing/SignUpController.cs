using System;
using System.Configuration;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class SignUpController : GuiController
    {
        public SignUpController(Service service, User loggedUser) : base(service, loggedUser)
        {
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["signUpWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate
            {
                Close();
            };
        }

        public void SignUp(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GuiElements.GetObject("UsernameEntry")).Text;
                User user = Service.SignUpUser(username);
                new MainController(Service, user).Open();
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

        public void ToLogIn(object sender, EventArgs args)
        {
            new LogInController(Service, null).Open();
            Close();
        }
    }
}