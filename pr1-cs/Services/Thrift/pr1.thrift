struct UserDto {
    1: string username;
    2: string password;
}

struct GameDto {
    1: i32 gameId;
    2: string name;
    3: string homeTeam;
    4: string awayTeam;
    5: i32 availableSeats;
    6: i32 seatCost;
}

service ThriftServices {
    UserDto signInUser(1: string username, 2: string password);

    oneway void signOutUser(1: string username);

    UserDto signUpUser(1: string username, 2: string password);

    list<GameDto> getAllGames();

    void sellSeats(1: GameDto game, 2: string clientName, 3: i32 seatsCount);

    list<GameDto> getGamesWithAvailableSeatsDescending();
}
