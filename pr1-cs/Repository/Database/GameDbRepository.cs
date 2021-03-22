using System.Collections.Generic;
using System.Data;
using log4net;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;

namespace pr1_cs.Repository.Database
{
    public class GameDbRepository : IGameRepository
    {
        private static readonly ILog Log = LogManager.GetLogger("GameDbRepository");

        public GameDbRepository()
        {
            Log.Info("Creating GameDbRepository");
        }

        public IEnumerable<Game> GetAll()
        {
            Log.Info("Entering GetAll");

            var games = new List<Game>();
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from games;";
                GetGamesFromDatabase(command, games);
            }

            Log.InfoFormat("Exiting GetAll with values {0}", games);
            return games;
        }

        public Game GetOne(int id)
        {
            Log.InfoFormat("Entering GetOne with value {0}", id);

            Game game;
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from games where gameId = " + id + ";";
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                        game = GetGameFromDataReader(dataReader);
                    else
                        throw new NotFoundException();
                }
            }

            Log.InfoFormat("Exiting GetOne with value {0}", game);
            return game;
        }

        public Game Add(Game game)
        {
            Log.InfoFormat("Entering Add with value {0}", game);

            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText =
                    "insert into games(gameId, homeTeam, awayTeam, availableSeats, name, seatCost) values(@gameId, @homeTeam, @awayTeam, @availableSeats, @name, @seatCost);";

                var dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@gameId";
                dataParameter.Value = game.Id;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@name";
                dataParameter.Value = game.Name;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@homeTeam";
                dataParameter.Value = game.HomeTeam;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@awayTeam";
                dataParameter.Value = game.AwayTeam;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@availableSeats";
                dataParameter.Value = game.AvailableSeats;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@seatCost";
                dataParameter.Value = game.SeatCost;
                command.Parameters.Add(dataParameter);

                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new DuplicateException();
            }

            Log.InfoFormat("Exiting getOne with value {0}", game);
            return game;
        }

        public Game Remove(int id)
        {
            Log.InfoFormat("Entering Remove with value {0}", id);

            var game = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "delete from games where gameId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting Remove with value {0}", game);
            return game;
        }

        public Game Modify(int id, Game newGame)
        {
            Log.InfoFormat("Entering Modify with value {0}", id);

            var oldGame = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "update games set " +
                                      "name = '" + newGame.Name +
                                      "', homeTeam = '" + newGame.HomeTeam +
                                      "', awayTeam = '" + newGame.AwayTeam +
                                      "', availableSeats = " + newGame.AvailableSeats +
                                      ", seatCost = " + newGame.SeatCost +
                                      " where gameId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting Remove with value {0}", oldGame);
            return oldGame;
        }

        public IEnumerable<Game> GetGamesByAvailableSeatsDescending(bool reverse)
        {
            Log.Info("Entering GetGamesByAvailableSeatsDescending");

            var games = new List<Game>();
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from games order by availableSeats";
                command.CommandText += reverse ? " desc;" : ";";
                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read()) games.Add(GetGameFromDataReader(dataReader));
                }
            }

            Log.InfoFormat("Exiting GetGamesByAvailableSeatsDescending with values {0}", games);
            return games;
        }

        public Game SetGameAvailableSeats(int id, int availableSeats)
        {
            Log.InfoFormat("Entering SetGameAvailableSeats with value {0}", id);

            var game = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "update games set availableSeats = " + availableSeats + " where gameId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting SetGameAvailableSeats with value {0}", game);
            return game;
        }

        private void GetGamesFromDatabase(IDbCommand command, List<Game> games)
        {
            using (var dataReader = command.ExecuteReader())
            {
                while (dataReader.Read()) games.Add(GetGameFromDataReader(dataReader));
            }
        }

        public static Game GetGameFromDataReader(IDataReader dataReader)
        {
            var gameId = dataReader.GetInt32(dataReader.GetOrdinal("gameId"));
            var name = dataReader.GetString(dataReader.GetOrdinal("name"));
            var homeTeam = dataReader.GetString(dataReader.GetOrdinal("homeTeam"));
            var awayTeam = dataReader.GetString(dataReader.GetOrdinal("awayTeam"));
            var availableSeats = dataReader.GetInt32(dataReader.GetOrdinal("availableSeats"));
            var seatCost = dataReader.GetInt32(dataReader.GetOrdinal("seatCost"));

            return new Game(gameId, name, homeTeam, awayTeam, availableSeats, seatCost);
        }

        public override string ToString()
        {
            var stringRepo = "";
            foreach (var game in GetAll()) stringRepo += game + "\n";
            return stringRepo;
        }
    }
}