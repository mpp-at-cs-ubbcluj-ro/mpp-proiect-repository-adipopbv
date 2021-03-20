using System;

namespace pr1_cs.Domain.Exceptions
{
    public class NotFoundException : Exception
    {
        public NotFoundException() : base("element not found")
        {
        }

        public NotFoundException(string message) : base(message)
        {
        }
    }
}