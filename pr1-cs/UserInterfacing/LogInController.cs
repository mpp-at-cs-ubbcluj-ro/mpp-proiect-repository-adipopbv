using System;
using System.Configuration;
using GLib;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class LogInController : GuiController
    {
        public LogInController(Service service, User loggedUser) : base(service, loggedUser)
        {
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["logInWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate
            {
                Close();
            };
        }

        public void LogIn(object sender, EventArgs args)
        {
            try
            {
                var username = ((Entry) GuiElements.GetObject("UsernameEntry")).Text;
                new MainController(Service, Service.LogInUser(username)).Open();
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
            new SignUpController(Service, null).Open();
            Close();
        }
    }
}