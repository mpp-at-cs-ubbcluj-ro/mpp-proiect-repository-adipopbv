using System.Collections.Generic;
using System.Linq;
using Model;
using Model.Exceptions;
using Mono.Data.Sqlite;
using DuplicateException = Model.Exceptions.Thrift.DuplicateException;
using NotFoundException = Model.Exceptions.Thrift.NotFoundException;

namespace Persistence.Database
{
    public class TicketDbRepository : ITicketRepository
    {
        public IEnumerable<Ticket> GetAll()
        {
            var tickets = new List<Ticket>();
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "select * from tickets inner join games on games.gameId = tickets.forGameId;";
            using var dataReader = command.ExecuteReader();
            while (dataReader.Read())
            {
                var ticketId = dataReader.GetInt32(dataReader.GetOrdinal("ticketId"));
                var clientName = dataReader.GetString(dataReader.GetOrdinal("clientName"));

                var ticket = new Ticket(ticketId, GameDbRepository.GetGameFromDataReader(dataReader),
                    clientName);
                tickets.Add(ticket);
            }

            return tickets;
        }

        public Ticket GetOne(int id)
        {
            Ticket ticket;
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText =
                "select * from tickets inner join games on games.gameId = tickets.forGameId where ticketId = " +
                id + ";";
            using var dataReader = command.ExecuteReader();
            if (dataReader.Read())
            {
                var ticketId = dataReader.GetInt32(dataReader.GetOrdinal("ticketId"));
                var clientName = dataReader.GetString(dataReader.GetOrdinal("clientName"));

                ticket = new Ticket(ticketId, GameDbRepository.GetGameFromDataReader(dataReader), clientName);
            }
            else
            {
                throw new NotFoundException();
            }
            return ticket;
        }

        public Ticket Add(Ticket ticket)
        {
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText =
                "insert into tickets(forGameId, clientName) values(@forGameId, @clientName);";

            var dataParameter = command.CreateParameter();
            dataParameter.ParameterName = "@forGameId";
            dataParameter.Value = ticket.ForGame.Id;
            command.Parameters.Add(dataParameter);

            dataParameter = command.CreateParameter();
            dataParameter.ParameterName = "@clientName";
            dataParameter.Value = ticket.ClientName;
            command.Parameters.Add(dataParameter);

            try
            {
                command.ExecuteNonQuery();
            }
            catch (SqliteException exception)
            {
                throw new DuplicateException();
            }

            command.CommandText = "select max(ticketId) as ticketId from tickets;";
            using var dataReader = command.ExecuteReader();
            dataReader.Read();
            var ticketId = dataReader.GetInt32(dataReader.GetOrdinal("ticketId"));
            ticket.Id = ticketId;
            return ticket;
        }

        public Ticket Remove(int id)
        {
            var ticket = GetOne(id);
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "delete from tickets where ticketId = " + id + ";";
            var result = command.ExecuteNonQuery();
            if (result == 0)
                throw new NotFoundException();
            return ticket;
        }

        public Ticket Modify(int id, Ticket newTicket)
        {
            var oldTicket = GetOne(id);
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "update tickets set " +
                                  "forGameId = " + newTicket.ForGame.Id +
                                  ", clientName = '" + newTicket.ClientName +
                                  "' where ticketId = " + id + ";";
            var result = command.ExecuteNonQuery();
            if (result == 0)
                throw new NotFoundException();
            return oldTicket;
        }

        public override string ToString()
        {
            return GetAll().Aggregate("", (current, ticket) => current + (ticket + "\n"));
        }
    }
}