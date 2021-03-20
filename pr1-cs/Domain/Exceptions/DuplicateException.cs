using System;

namespace pr1_cs.Domain.Exceptions
{
    public class DuplicateException : Exception
    {
        public DuplicateException() : base("duplicate element")
        {
        }

        public DuplicateException(string message) : base(message)
        {
        }
    }
}