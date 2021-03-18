using System.Collections.Generic;
using System.Data;
using log4net;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;

namespace pr1_cs.Repository.Database
{
    public class TicketDbRepository : ITicketRepository
    {
        private static readonly ILog Log = LogManager.GetLogger("TicketDbRepository");

        public TicketDbRepository()
        {
            Log.Info("Creating TicketDbRepository");
        }

        public IEnumerable<Ticket> GetAll()
        {
            Log.Info("Entering GetAll");

            var tickets = new List<Ticket>();
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from tickets inner join games on games.gameId = tickets.forGameId;";
                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        var ticketId = dataReader.GetInt32(dataReader.GetOrdinal("ticketId"));
                        var clientName = dataReader.GetString(dataReader.GetOrdinal("clientName"));

                        var ticket = new Ticket(ticketId, getGameFromDataReader(dataReader), clientName);
                        tickets.Add(ticket);
                    }
                }
            }

            Log.InfoFormat("Exiting GetAll with values {0}", tickets);
            return tickets;
        }

        public Ticket GetOne(int id)
        {
            Log.InfoFormat("Entering GetOne with value {0}", id);

            Ticket ticket;
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from tickets inner join games on games.gameId = tickets.forGameId where ticketId = " + id + ";";
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        var ticketId = dataReader.GetInt32(dataReader.GetOrdinal("ticketId"));
                        var clientName = dataReader.GetString(dataReader.GetOrdinal("clientName"));

                        ticket = new Ticket(ticketId, getGameFromDataReader(dataReader), clientName);
                    }
                    else
                    {
                        throw new NotFoundException();
                    }
                }
            }

            Log.InfoFormat("Exiting GetOne with value {0}", ticket);
            return ticket;
        }

        public Ticket Add(Ticket ticket)
        {
            Log.InfoFormat("Entering Add with value {0}", ticket);

            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText =
                    "insert into tickets(ticketId, forGameId, clientName) values(@ticketId, @forGameId, @clientName);";

                var dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@ticketId";
                dataParameter.Value = ticket.Id;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@forGameId";
                dataParameter.Value = ticket.ForGame.Id;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@clientName";
                dataParameter.Value = ticket.ClientName;
                command.Parameters.Add(dataParameter);

                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new DuplicateException();
            }

            Log.InfoFormat("Exiting getOne with value {0}", ticket);
            return ticket;
        }

        public Ticket Remove(int id)
        {
            Log.InfoFormat("Entering Remove with value {0}", id);

            var ticket = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "delete from tickets where ticketId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting Remove with value {0}", ticket);
            return ticket;
        }

        public Ticket Modify(int id, Ticket newTicket)
        {
            Log.InfoFormat("Entering Modify with value {0}", id);

            var oldTicket = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "update tickets set " +
                                      "forGameId = " + newTicket.ForGame.Id +
                                      ", clientName = '" + newTicket.ClientName +
                                      "' where ticketId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting Remove with value {0}", oldTicket);
            return oldTicket;
        }

        private Game getGameFromDataReader(IDataReader dataReader)
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
            foreach (var ticket in GetAll()) stringRepo += ticket + "\n";
            return stringRepo;
        }
    }
}