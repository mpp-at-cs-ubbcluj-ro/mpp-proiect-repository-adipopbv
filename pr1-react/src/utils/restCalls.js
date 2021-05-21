import {BASE_URL} from './consts';

function status(response) {
    console.log('response status ' + response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}

export function getGames() {
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    let init = {
        method: 'GET',
        headers: headers,
        mode: 'cors'
    };

    return fetch(new Request(BASE_URL, init))
        .then(status)
        .then(json)
        .then(data => {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(error => {
            console.log('Request failed', error);
            return error;
        });
}

export function deleteGame(id) {
    let headers = new Headers();
    headers.append("Accept", "application/json");

    let init = {
        method: 'DELETE',
        headers: headers,
        mode: 'cors'
    };

    return fetch(BASE_URL + '/' + id, init)
        .then(status)
        .then(response => {
            console.log('Delete status ' + response.status);
            return response.text();
        }).catch(e => {
            console.log('error ' + e);
            return Promise.reject(e);
        });
}

export function addGame(user) {
    let header = new Headers();
    header.append("Accept", "application/json");
    header.append("Content-Type", "application/json");

    let init = {
        method: 'POST',
        headers: header,
        mode: 'cors',
        body: JSON.stringify(user)
    };

    return fetch(BASE_URL, init)
        .then(status)
        .then(response => {
            return response.text();
        }).catch(error => {
            console.log('Request failed', error);
            return Promise.reject(error);
        });
}
