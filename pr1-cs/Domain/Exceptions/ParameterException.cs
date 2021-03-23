using System;

namespace pr1_cs.Domain.Exceptions
{
    public class ParameterException : Exception
    {
        public ParameterException() : base("parameter error")
        {
        }

        public ParameterException(string? message) : base(message)
        {
        }
    }
}