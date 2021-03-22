using System;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class SignUpController : UserInterface
    {
        public SignUpController(Service service, User loggedUser) : base(service, loggedUser)
        {
            OwnedWindow = (Window) GtkClient.ClientElements.GetObject("SignUpWindow");
            OwnedWindow.DeleteEvent += delegate
            {
                GtkClient.OpenWindows--;
                if (GtkClient.OpenWindows <= 0)
                    Application.Quit();
            };
        }

        public void SignUp(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GtkClient.ClientElements.GetObject("SignUpUsernameEntry")).Text;
                Service.SignUpUser(username);
                Close();
            }
            catch (DuplicateException exception)
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
            var logInController = new LogInController(Service, null);
            logInController.Open();
            Close();
        }
    }
}