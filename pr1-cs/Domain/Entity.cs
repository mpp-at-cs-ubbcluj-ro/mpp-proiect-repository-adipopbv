namespace pr1_cs.Domain
{
    public abstract class Entity<TId>
    {
        private readonly TId _id;

        public TId Id => _id;

        protected Entity(TId id)
        {
            _id = id;
        }
    }
}