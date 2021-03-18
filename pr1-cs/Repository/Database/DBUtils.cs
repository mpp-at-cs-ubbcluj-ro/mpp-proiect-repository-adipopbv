using System.Data;
using pr1_cs.DbUtils;

namespace pr1_cs.Repository.Database
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
            return ConnectionFactory.Instance.CreateConnection();
        }
    }
}