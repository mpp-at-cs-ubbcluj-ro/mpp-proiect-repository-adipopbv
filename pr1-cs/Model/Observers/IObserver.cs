namespace Model.Observers
{
    public interface IObserver
    {
        void SeatsSold(int gameId, int seatsCount);
    }
}