using System;
using System.Configuration;
using Gtk;
using Model;
using Model.Exceptions;
using Model.Observers;
using Services.Thrift;
using Services.Thrift.DataTransfer;
using Thrift.Transport;

namespace Client.Gtk.Thrift.Clients
{
    public class MainWindow : Window, IObserver
    {
        private readonly ListStore _gamesModel = new(
            typeof(string),
            typeof(string),
            typeof(string),
            typeof(string),
            typeof(string),
            typeof(Game));

        private TreeView _gamesTreeView;
        private bool _switchFilter;

        public void SeatsSold(int gameId, int seatsCount)
        {
            LoadGameTableData();
        }

        public override Window Init(TTransport connection, ThriftServices.Client services, User signedInUser)
        {
            base.Init(connection, services, signedInUser);
            GuiElements.AddFromFile(ConfigurationManager.AppSettings["mainWindow"]);
            GuiElements.Autoconnect(this);
            OwnedWindow = (global::Gtk.Window) GuiElements.GetObject("Window");
            OwnedWindow.DeleteEvent += delegate { Close(); };

            _gamesTreeView = (TreeView) GuiElements.GetObject("GamesTreeView");
            LoadGameTableData();
            _gamesTreeView.Model = _gamesModel;

            _gamesTreeView.AppendColumn("Name", new CellRendererText(), "text", 0);
            _gamesTreeView.AppendColumn("Home team", new CellRendererText(), "text", 1);
            _gamesTreeView.AppendColumn("Away team", new CellRendererText(), "text", 2);
            _gamesTreeView.AppendColumn("Seat cost", new CellRendererText(), "text", 3);
            _gamesTreeView.AppendColumn("Available seats", new CellRendererText(), "text", 4);

            return this;
        }

        private async void LoadGameTableData()
        {
            _gamesModel.Clear();
            foreach (var game in _switchFilter
                ? await Services.getGamesWithAvailableSeatsDescending()
                : await Services.getAllGames())
                _gamesModel.AppendValues(game.Name, game.HomeTeam, game.AwayTeam, game.SeatCost.ToString(),
                    game.AvailableSeats <= 0 ? "SOLD OUT" : game.AvailableSeats.ToString(), game);
        }

        public async void SellSeats(object sender, EventArgs args)
        {
            try
            {
                _gamesTreeView.Selection.GetSelected(out var model, out var iter);
                if (model == null)
                    throw new NotFoundException("no game selected");

                var clientName = ((Entry) GuiElements.GetObject("ClientNameEntry")).Text;
                var seatsCount = (int) ((SpinButton) GuiElements.GetObject("SeatsCountSpinButton")).Value;
                var game = (Game) model.GetValue(iter, 5);
                await Services.sellSeats(DtoUtils.ToDto(game), clientName, seatsCount);

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

            LoadGameTableData();
        }

        public void SwitchFilter(object sender, EventArgs args)
        {
            _switchFilter = !_switchFilter;
            LoadGameTableData();
        }

        public async void SignOut(object sender, EventArgs args)
        {
            try
            {
                await Services.signOutUser(SignedInUser.Username);
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