using System;

namespace Model.Exceptions
{
    public class SignInException : Exception
    {
        public SignInException() : base("sign in error")
        {
        }

        public SignInException(String message) : base(message)
        {
        }
    }
}