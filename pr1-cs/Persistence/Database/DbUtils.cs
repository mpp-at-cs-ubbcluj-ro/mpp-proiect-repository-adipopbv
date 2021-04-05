using System;
using System.Configuration;
using System.Data;
using Model.Exceptions;
using Mono.Data.Sqlite;

namespace Persistence.Database
{
    public static class DbUtils
    {
        private static IDbConnection _connection;

        public static IDbConnection Connection
        {
            get
            {
                if (_connection == null || _connection.State == ConnectionState.Closed)
                {
                    _connection = GetNewConnection();
                    _connection.Open();
                    using var command = _connection.CreateCommand();
                    command.CommandText = "pragma foreign_keys = on;";
                    command.ExecuteNonQuery();
                }

                return _connection;
            }
        }

        private static IDbConnection GetNewConnection()
        {
            try
            {
                return new SqliteConnection(ConfigurationManager.AppSettings["sqliteConnectionString"]);
            }
            catch (Exception exception)
            {
                throw new DatabaseException("database error: " + exception);
            }
        }
    }
}