using System;
using System.Configuration;
using Gtk;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Services;

namespace pr1_cs.UserInterfacing
{
    public class MainController : GuiController
    {
        private readonly TreeView _gamesTreeView;
        private ListStore _gamesModel;

        public MainController(Service service, User loggedUser) : base(service, loggedUser)
        {
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["mainWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate { Close(); };

            _gamesTreeView = (TreeView) GuiElements.GetObject("GamesTreeView");
            Update(this, EventArgs.Empty);
            _gamesTreeView.Model = _gamesModel;
        }

        public void SellSeats(object sender, EventArgs args)
        {
            try
            {
                ITreeModel model;
                TreeIter iter;
                _gamesTreeView.Selection.GetSelected(out model, out iter);
                if (model == null)
                    throw new NotFoundException("no game selected");
                var clientName = ((Entry) GuiElements.GetObject("ClientNameEntry")).Text;
                var seatsCount = ((SpinButton) GuiElements.GetObject("ClientNameEntry")).ValueAsInt;
                int id = 0, seatCost = 0, availableSeats = 0;
                string name, homeTeam, awayTeam;
                // model.GetValue(iter, 0, ref id);
                // Service.SellSeats(model[iter][0]);
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

        public void Update(object sender, EventArgs args)
        {
            _gamesModel = new ListStore(typeof(int), typeof(string), typeof(string), typeof(string), typeof(string), typeof(string));
            foreach (var game in Service.GetAllGames())
                _gamesModel.AppendValues(game.Id, game.Name, game.HomeTeam, game.AwayTeam, game.SeatCost.ToString(),
                    game.AvailableSeats.ToString());
        }
    }
}