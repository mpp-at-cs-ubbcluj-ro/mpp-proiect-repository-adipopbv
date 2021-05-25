import React from 'react';
import GameTable from './Game';
import './App.css'
import GameForm from "./GameForm";
import {addGame, deleteGame, getGames, modifyGame} from './utils/restCalls'


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            // games: [{"id": "100", "name": "Game 100", "homeTeam": "Some team", "awayTeam": "Some other team", "availableSeats": 1, "seatCost": 1000}],
            games: [],
            deleteFunc: this.deleteFunc.bind(this),
            addFunc: this.addFunc.bind(this),
        }
    }

    addFunc(game) {
        addGame(game)
            .then(() => getGames())
            .then(games => this.setState({games}))
            .catch(error => console.log('Error when adding: ', error));
    }

    modifyFunc(id, game) {
        modifyGame(id, game)
            .then(() => getGames())
            .then(games => this.setState({games}))
            .catch(error => console.log('Error when modifying: ', error));
    }

    deleteFunc(id) {
        deleteGame(id)
            .then(() => getGames())
            .then(games => this.setState({games}))
            .catch(error => console.log('Error when deleting: ', error));
    }

    componentDidMount() {
        getGames().then(games => this.setState({games}));
    }

    render() {
        return (
            <div className="App">
                <h1>Basketball games management app</h1>
                <GameForm addFunc={this.state.addFunc} modifyFunc={this.state.modifyFunc}/>
                <br/>
                <GameTable games={this.state.games} deleteFunc={this.state.deleteFunc}/>
            </div>
        );
    }
}

export default App;
