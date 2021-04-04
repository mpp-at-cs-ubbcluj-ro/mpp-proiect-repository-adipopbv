using System;

namespace Model.Exceptions
{
    public class NetworkingException : Exception
    {
        public NetworkingException() : base("networking error")
        {
        }

        public NetworkingException(string message) : base(message)
        {
        }
    }
}