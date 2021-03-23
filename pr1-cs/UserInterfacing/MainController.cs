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
        private readonly ListStore _gamesModel = new ListStore(typeof(string), typeof(string), typeof(string),
            typeof(string),
            typeof(string), typeof(Game));

        private readonly TreeView _gamesTreeView;
        private bool _switchFilter;

        public MainController(Service service, User loggedUser) : base(service, loggedUser)
        {
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["mainWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate { Close(); };

            _gamesTreeView = (TreeView) GuiElements.GetObject("GamesTreeView");
            Update();
            _gamesTreeView.Model = _gamesModel;

            _gamesTreeView.AppendColumn("Name", new CellRendererText(), "text", 0);
            _gamesTreeView.AppendColumn("Home team", new CellRendererText(), "text", 1);
            _gamesTreeView.AppendColumn("Away team", new CellRendererText(), "text", 2);
            _gamesTreeView.AppendColumn("Seat cost", new CellRendererText(), "text", 3);
            _gamesTreeView.AppendColumn("Available seats", new CellRendererText(), "text", 4);
        }

        public void SellSeats(object sender, EventArgs args)
        {
            try
            {
                _gamesTreeView.Selection.GetSelected(out var model, out var iter);
                if (model == null)
                    throw new NotFoundException("no game selected");

                var clientName = ((Entry) GuiElements.GetObject("ClientNameEntry")).Text;
                var seatsCount = (int) ((SpinButton) GuiElements.GetObject("SeatsCountSpinButton")).Value;
                var game = (Game) model.GetValue(iter, 5);
                Service.SellSeats(game, clientName, seatsCount);

                ((Entry) GuiElements.GetObject("ClientNameEntry")).Text = "";
                ((SpinButton) GuiElements.GetObject("SeatsCountSpinButton")).Value = 1;

                var dialog = new MessageDialog(OwnedWindow,
                    DialogFlags.DestroyWithParent, MessageType.Info,
                    ButtonsType.Close, "seats sold successfully");
                dialog.Run();
                dialog.Destroy();
            }
            catch (Exception exception)
            {
                var dialog = new MessageDialog(OwnedWindow,
                    DialogFlags.DestroyWithParent, MessageType.Error,
                    ButtonsType.Close, exception.Message);
                dialog.Run();
                dialog.Destroy();
            }

            Update();
        }

        public void SwitchFilter(object sender, EventArgs args)
        {
            _switchFilter = !_switchFilter;
            Update();
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

        public void Update()
        {
            _gamesModel.Clear();
            foreach (var game in _switchFilter ? Service.GetGamesWithAvailableSeatsDescending() : Service.GetAllGames())
                _gamesModel.AppendValues(game.Name, game.HomeTeam, game.AwayTeam, game.SeatCost.ToString(),
                    game.AvailableSeats <= 0 ? "SOLD OUT" : game.AvailableSeats.ToString(), game);
        }
    }
}