using System;

namespace Networking.DataTransfer
{
    [Serializable]
    public class UserDto
    {
        public UserDto(string username, string password)
        {
            Username = username;
            Password = password;
        }

        public string Username { get; }
        public string Password { get; }

        public override string ToString()
        {
            return "UserDto[" + Username + " " + Password + "]";
        }
    }
}