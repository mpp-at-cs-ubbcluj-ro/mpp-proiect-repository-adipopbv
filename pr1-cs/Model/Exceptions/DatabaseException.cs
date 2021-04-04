using System;

namespace Model.Exceptions
{
    public class DatabaseException : Exception
    {
        public DatabaseException() : base("database error")
        {
        }

        public DatabaseException(string message) : base(message)
        {
        }
    }
}