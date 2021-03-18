using System.Collections.Generic;
using pr1_cs.Domain;

namespace pr1_cs.Repository
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