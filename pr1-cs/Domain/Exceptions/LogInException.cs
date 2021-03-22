using System;

namespace pr1_cs.Domain.Exceptions
{
    public class LogInException : Exception
    {
        public LogInException() : base("log in error")
        {
        }

        public LogInException(String message) : base(message)
        {
        }
    }
}