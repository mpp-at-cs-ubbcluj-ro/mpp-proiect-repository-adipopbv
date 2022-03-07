import React from 'react';
import './App.css'

class GameRow extends React.Component {

    handleClick = (event) => {
        this.props.deleteFunc(this.props.game.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.game.id}</td>
                <td>{this.props.game.name}</td>
                <td>{this.props.game.homeTeam}</td>
                <td>{this.props.game.awayTeam}</td>
                <td>{this.props.game.availableSeats}</td>
                <td>{this.props.game.seatCost}</td>
                <td>
                    <button onClick={this.handleClick}>Delete</button>
                </td>
            </tr>
        );
    }
}

class GameTable extends React.Component {
    render() {
        let rows = [];
        let deleteFunc = this.props.deleteFunc;
        Array.prototype.forEach.call(this.props.games, game => {
            rows.push(<GameRow game={game} key={game.id} deleteFunc={deleteFunc}/>);
        });
        return (
            <div className="GameTable">
                <table className="center">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>HomeTeam</th>
                        <th>AwayTeam</th>
                        <th>AvailableSeats</th>
                        <th>SeatCost</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        );
    }
}

export default GameTable;
