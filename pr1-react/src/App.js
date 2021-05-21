import React from 'react';
import GameTable from './Game';
import './App.css'
import GameForm from "./GameForm";
import {addGame, deleteGame, getGames} from './utils/restCalls'


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            games: [],
            // games: [{"passwd": "maria", "name": "Marinescu Maria", "id": "maria"}],
            deleteFunc: this.deleteFunc.bind(this),
            addFunc: this.addFunc.bind(this),
        }
    }

    addFunc(user) {
        addGame(user)
            .then(res => getGames())
            .then(games => this.setState({games}))
            .catch(error => console.log('Error when adding: ', error));
    }

    deleteFunc(user) {
        deleteGame(user)
            .then(res => getGames())
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
                <GameForm addFunc={this.state.addFunc}/>
                <br/>
                <br/>
                <GameTable games={this.state.games} deleteFunc={this.state.deleteFunc}/>
            </div>
        );
    }
}

export default App;
