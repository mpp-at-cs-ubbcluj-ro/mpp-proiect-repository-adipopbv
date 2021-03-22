using System;
using System.Configuration;
using GLib;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class MainController : GuiController
    {
        public MainController(Service service, User loggedUser) : base(service, loggedUser)
        {
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["mainWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate
            {
                Close();
            };
        }

        public void LogOut(object sender, EventArgs args)
        {
            try
            {
                Service.LogOutUser(LoggedUser.Username);
                var dialog = new MessageDialog(OwnedWindow,
                    DialogFlags.DestroyWithParent, MessageType.Info,
                    ButtonsType.Close, "logged out successfully");
                dialog.Run();
                dialog.Destroy();
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
    }
}