using System;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class LogInController : UserInterface
    {
        public LogInController(Service service, User loggedUser) : base(service, loggedUser)
        {
            OwnedWindow = (Window) GtkClient.ClientElements.GetObject("LogInWindow");
            OwnedWindow.DeleteEvent += delegate
            {
                GtkClient.OpenWindows--;
                if (GtkClient.OpenWindows <= 0)
                    Application.Quit();
            };
        }

        public void LogIn(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GtkClient.ClientElements.GetObject("LogInUsernameEntry")).Text;
                LoggedUser = Service.LogInUser(username);
                Close();
            }
            catch (NotFoundException exception)
            {
                var dialog = new MessageDialog(OwnedWindow,
                    DialogFlags.DestroyWithParent, MessageType.Error,
                    ButtonsType.Close, exception.Message);
                dialog.Run();
                dialog.Destroy();
            }
            catch (LogInException exception)
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
            var signUpController = new SignUpController(Service, null);
            signUpController.Open();
            Close();
        }
    }
}