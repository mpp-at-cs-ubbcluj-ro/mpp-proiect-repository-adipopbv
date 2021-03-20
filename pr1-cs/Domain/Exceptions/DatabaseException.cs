using System;

namespace pr1_cs.Domain.Exceptions
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