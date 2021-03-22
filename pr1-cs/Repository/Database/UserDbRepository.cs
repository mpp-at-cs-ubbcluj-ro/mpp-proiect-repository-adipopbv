using System.Collections.Generic;
using log4net;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;

namespace pr1_cs.Repository.Database
{
    public class UserDbRepository : IUserRepository
    {
        private static readonly ILog Log = LogManager.GetLogger("UserDbRepository");

        public UserDbRepository()
        {
            Log.Info("Creating UserDbRepository");
        }

        public IEnumerable<User> GetAll()
        {
            Log.Info("Entering GetAll");

            var users = new List<User>();
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from users;";
                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        var userId = dataReader.GetInt32(dataReader.GetOrdinal("userId"));
                        var username = dataReader.GetString(dataReader.GetOrdinal("username"));
                        var status = dataReader.GetString(dataReader.GetOrdinal("status"));

                        var user = new User(userId, username, status);
                        users.Add(user);
                    }
                }
            }

            Log.InfoFormat("Exiting GetAll with values {0}", users);
            return users;
        }

        public User GetOne(int id)
        {
            Log.InfoFormat("Entering GetOne with value {0}", id);

            User user;
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from users where userId = " + id + ";";
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        var username = dataReader.GetString(dataReader.GetOrdinal("username"));
                        var status = dataReader.GetString(dataReader.GetOrdinal("status"));

                        user = new User(id, username, status);
                    }
                    else
                    {
                        throw new NotFoundException();
                    }
                }
            }

            Log.InfoFormat("Exiting GetOne with value {0}", user);
            return user;
        }

        public User GetOne(string username)
        {
            Log.InfoFormat("Entering GetOne with value {0}", username);

            User user;
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from users where username = '" + username + "';";
                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        var userId = dataReader.GetInt32(dataReader.GetOrdinal("userId"));
                        var status = dataReader.GetString(dataReader.GetOrdinal("status"));

                        user = new User(userId, username, status);
                    }
                    else
                    {
                        throw new NotFoundException();
                    }
                }
            }

            Log.InfoFormat("Exiting GetOne with value {0}", user);
            return user;
        }

        public User Add(User user)
        {
            Log.InfoFormat("Entering Add with value {0}", user);

            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "insert into users(username, status) values(@username, @status);";

                var dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@username";
                dataParameter.Value = user.Username;
                command.Parameters.Add(dataParameter);

                dataParameter = command.CreateParameter();
                dataParameter.ParameterName = "@status";
                dataParameter.Value = user.Status;
                command.Parameters.Add(dataParameter);

                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new DuplicateException();

                command.CommandText = "select userId from users where username = '" + user.Username + "';";
                using (var dataReader = command.ExecuteReader())
                {
                    dataReader.Read();
                    var userId = dataReader.GetInt32(dataReader.GetOrdinal("userId"));
                    user.Id = userId;
                }
            }

            Log.InfoFormat("Exiting Add with value {0}", user);
            return user;
        }

        public User Remove(int id)
        {
            Log.InfoFormat("Entering Remove with value {0}", id);

            var user = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "delete from users where userId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting Remove with value {0}", user);
            return user;
        }

        public User Modify(int id, User newUser)
        {
            Log.InfoFormat("Entering Modify with value {0}", id);

            var oldUser = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "update users set " +
                                      "username = '" + newUser.Username +
                                      "', status = '" + newUser.Status +
                                      "' where userId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting Remove with value {0}", oldUser);
            return oldUser;
        }

        public User SetUserStatus(int id, string status)
        {
            Log.InfoFormat("Entering SetUserStatus with value {0}", id);

            var user = GetOne(id);
            var connection = DbUtils.Connection;
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "update users set status = '" + status + "' where userId = " + id + ";";
                var result = command.ExecuteNonQuery();
                if (result == 0)
                    throw new NotFoundException();
            }

            Log.InfoFormat("Exiting SetUserStatus with value {0}", user);
            return user;
        }

        public override string ToString()
        {
            var stringRepo = "";
            foreach (var user in GetAll()) stringRepo += user + "\n";
            return stringRepo;
        }
    }
}