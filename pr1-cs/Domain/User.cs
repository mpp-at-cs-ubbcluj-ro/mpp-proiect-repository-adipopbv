namespace pr1_cs.Domain
{
    public class User : Entity<int>
    {
        public User(string username)
        {
            Username = username;
            Status = "logged-out";
        }

        public User(string username, string status)
        {
            Username = username;
            Status = status;
        }

        public User(int id, string username, string status) : base(id)
        {
            Username = username;
            Status = status;
        }

        public string Username { get; set; }

        public string Status { get; set; }

        public override string ToString()
        {
            return "User{" +
                   "id='" + Id + '\'' +
                   ", username='" + Username + '\'' +
                   ", status='" + Status + '\'' +
                   '}';
        }
    }
}