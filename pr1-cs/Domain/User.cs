namespace pr1_cs.Domain
{
    public class User : Entity<int>
    {
        private string _username;
        private string _status;

        public string Username
        {
            get => _username;
            set => _username = value;
        }

        public string Status
        {
            get => _status;
            set => _status = value;
        }

        public User(int id, string username, string status) : base(id)
        {
            _username = username;
            _status = status;
        }

        public override string ToString()
        {
		    return "User{" +
                "id='" + Id+ '\'' +
                ", username='" + Username + '\'' +
                ", status='" + Status + '\'' +
                '}';
        }
    }
}