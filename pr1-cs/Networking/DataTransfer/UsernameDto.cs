using System;

namespace Networking.DataTransfer
{
    [Serializable]
    public class UsernameDto
    {
        public UsernameDto(string value)
        {
            Value = value;
        }

        public string Value { get; }

        public override string ToString()
        {
            return "UsernameDto[" + Value + "]";
        }
    }
}