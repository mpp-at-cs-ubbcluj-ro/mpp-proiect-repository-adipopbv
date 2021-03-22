using pr1_cs.Domain;

namespace pr1_cs.Repository
{
    public interface IUserRepository : IRepository<int, User>
    {
        User SetUserStatus(int id, string status);
        
        User GetOne(string username);
    }
}