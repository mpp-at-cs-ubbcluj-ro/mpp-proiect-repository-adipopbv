using System;

namespace Model.Exceptions
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