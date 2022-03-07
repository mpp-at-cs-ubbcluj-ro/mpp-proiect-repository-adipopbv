namespace Model
{
    public class User : Entity<string>
    {
        public User(string username, string password) : base(username)
        {
            Username = username;
            Password = password;
        }

        public string Username { get; set; }

        public string Password { get; set; }

        public override string ToString()
        {
            return "User{" +
                   "username='" + Username + '\'' +
                   '}';
        }
    }
}