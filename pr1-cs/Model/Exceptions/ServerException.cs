using System;

namespace Model.Exceptions
{
    public class ServerException : Exception
    {
        public ServerException() : base("server error")
        {
        }

        public ServerException(string message) : base(message)
        {
        }
    }
}