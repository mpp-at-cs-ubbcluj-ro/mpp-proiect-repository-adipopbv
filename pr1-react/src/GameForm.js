import React from 'react';

class GameForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {id: '', name: '', homeTeam: '', awayTeam: '', availableSeats: 0, seatCost: 0};
    }

    handleGameChange = (event) => {
        this.setState({id: event.target.value});
    }

    handleNameChange = (event) => {
        this.setState({name: event.target.value});
    }

    handleHomeTeamChange = (event) => {
        this.setState({homeTeam: event.target.value});
    }

    handleAwayTeamChange = (event) => {
        this.setState({awayTeam: event.target.value});
    }

    handleAvailableSeatsChange = (event) => {
        this.setState({availableSeats: event.target.value});
    }

    handleSeatCostChange = (event) => {
        this.setState({seatCost: event.target.value});
    }

    handleSubmit = (event) => {
        let game = {
            id: this.state.id,
            name: this.state.name,
            homeTeam: this.state.homeTeam,
            awayTeam: this.state.awayTeam,
            availableSeats: this.state.availableSeats,
            seatCost: this.state.seatCost
        }
        console.log('A game was submitted: ');
        console.log(game);
        this.props.addFunc(game);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Id:
                    <input type="text" value={this.state.id} onChange={this.handleGameChange}/>
                </label><br/>
                <label>
                    Name:
                    <input type="text" value={this.state.name} onChange={this.handleNameChange}/>
                </label><br/>
                <label>
                    HomeTeam:
                    <input type="text" value={this.state.homeTeam} onChange={this.handleHomeTeamChange}/>
                </label><br/>
                <label>
                    AwayTeam:
                    <input type="text" value={this.state.awayTeam} onChange={this.handleAwayTeamChange}/>
                </label><br/>
                <label>
                    AvailableSeats:
                    <input type="number" value={this.state.availableSeats} onChange={this.handleAvailableSeatsChange}/>
                </label><br/>
                <label>
                    SeatCost:
                    <input type="number" value={this.state.seatCost} onChange={this.handleSeatCostChange}/>
                </label><br/>

                <input type="submit" value="Submit"/>
            </form>
        );
    }
}

export default GameForm;
