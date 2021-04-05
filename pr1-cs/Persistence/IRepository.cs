using System.Collections.Generic;
using Model;

namespace Persistence
{
    public interface IRepository<TId, TEntity> where TEntity : Entity<TId>
    {
        IEnumerable<TEntity> GetAll();

        TEntity GetOne(TId id);

        TEntity Add(TEntity entity);
        
        TEntity Remove(TId id);
        
        TEntity Modify(TId id, TEntity newEntity);
    }
}