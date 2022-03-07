using System;

namespace Model.Exceptions
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