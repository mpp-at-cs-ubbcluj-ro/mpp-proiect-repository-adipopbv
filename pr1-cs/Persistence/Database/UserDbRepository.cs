using System.Collections.Generic;
using System.Linq;
using Model;
using Model.Exceptions;
using Mono.Data.Sqlite;

namespace Persistence.Database
{
    public class UserDbRepository : IUserRepository
    {
        public IEnumerable<User> GetAll()
        {
            var users = new List<User>();
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "select * from users;";
            using var dataReader = command.ExecuteReader();
            while (dataReader.Read())
            {
                var username = dataReader.GetString(dataReader.GetOrdinal("username"));
                var password = dataReader.GetString(dataReader.GetOrdinal("password"));

                var user = new User(username, password);
                users.Add(user);
            }

            return users;
        }

        public User GetOne(string username)
        {
            User user;
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "select * from users where username = '" + username + "';";
            using var dataReader = command.ExecuteReader();
            if (dataReader.Read())
            {
                var password = dataReader.GetString(dataReader.GetOrdinal("password"));

                user = new User(username, password);
            }
            else
            {
                throw new NotFoundException();
            }

            return user;
        }

        public User Add(User user)
        {
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "insert into users(username, password) values(@username, @password);";

            var dataParameter = command.CreateParameter();
            dataParameter.ParameterName = "@username";
            dataParameter.Value = user.Username;
            command.Parameters.Add(dataParameter);

            dataParameter = command.CreateParameter();
            dataParameter.ParameterName = "@password";
            dataParameter.Value = user.Password;
            command.Parameters.Add(dataParameter);

            try
            {
                command.ExecuteNonQuery();
            }
            catch (SqliteException exception)
            {
                throw new DuplicateException();
            }

            return user;
        }

        public User Remove(string username)
        {
            var user = GetOne(username);
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "delete from users where username = '" + username + "';";
            var result = command.ExecuteNonQuery();
            if (result == 0)
                throw new NotFoundException();
            return user;
        }

        public User Modify(string username, User newUser)
        {
            var oldUser = GetOne(username);
            var connection = DbUtils.Connection;
            using var command = connection.CreateCommand();
            command.CommandText = "update users set " +
                                  "password = '" + newUser.Password +
                                  "' where username = '" + username + "';";
            var result = command.ExecuteNonQuery();
            if (result == 0)
                throw new NotFoundException();
            return oldUser;
        }
        
        public override string ToString()
        {
            return GetAll().Aggregate("", (current, user) => current + (user + "\n"));
        }
    }
}