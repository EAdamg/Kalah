let gameId;
let aiPlayer;

window.onload = function() {
    hideDifficulty();
};

function toggleDifficulty(display) {
    document.getElementById("diffRadio").style.display = display;
    document.getElementById("diffHr").style.display = "none" === display ? "none" : "block";
}

function showDifficulty() {
    toggleDifficulty("inline");
}

function hideDifficulty() {
    toggleDifficulty("none");
}

function sendHttpRequest(uri, method, headers, data, func) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            let response = JSON.parse(this.responseText);
            if (this.status === 200) {
                func(response);
            } else {
                document.getElementById("response").innerHTML = response["message"];
            }
        }
    };
    xhttp.open(method, uri, true);
    if (headers !== null) {
        Object.keys(headers).forEach(function(header) {
            xhttp.setRequestHeader(header, headers[header]);
        });
    }
    if (data !== null) {
        xhttp.send(JSON.stringify(data));
    } else {
        xhttp.send();
    }
}

function createGameFunction(response) {
    let message;
    gameId = response["id"];
    for (let i = 1; i < 7; ++i) {
        document.getElementById(i.toString()).innerText = document.querySelector('input[name="numStones"]:checked').value;
    }
    for (i = 8; i < 14; ++i) {
        document.getElementById(i.toString()).innerText = document.querySelector('input[name="numStones"]:checked').value;
    }
    document.getElementById("input").innerHTML = "<p id=\"response\"></p>";
    for (i = 1; i < 15; ++i) {
        document.getElementById(i.toString()).addEventListener("click", function() {
            makeMove(this.id);
        })
    }
    if ("1" === aiPlayer) {
        message = "Waiting for Player 1...";
        makeAiMove();
    } else {
        message = "It is now Player 1's turn";
    }
    document.getElementById("response").innerHTML = message;
}

function makeMoveFunction(response) {
    let message;
    let boardStatus = response["status"];
    for (let i = 1; i < 15; ++i) {
        document.getElementById(i.toString()).innerText = boardStatus[i];
    }
    let nextPlayer = response["nextPlayer"];
    if (nextPlayer !== null) {
        if ("1" === aiPlayer && "Player 1" === nextPlayer || "2" === aiPlayer && "Player 2" === nextPlayer) {
            message = "Waiting for " + nextPlayer + "...";
            makeAiMove();
        } else {
            message = "It is now " + nextPlayer + "'s turn"
        }
    } else {
        message = response["message"];
    }
    document.getElementById("response").innerHTML = message;
}

function createGame() {
    aiPlayer = document.querySelector('input[name="ai"]:checked').value;
    let numStonesPerPit = document.querySelector('input[name="numStones"]:checked').value;
    let difficulty = document.querySelector('input[name="diff"]:checked').value;
    let data = {
        "numStonesPerPit": numStonesPerPit,
        "difficulty": difficulty
    };
    sendHttpRequest("http://localhost:8080/games","POST", {"Content-Type": "application/json"}, data, createGameFunction)
}


function makeMove(pitNum) {
    sendHttpRequest("http://localhost:8080/games/" + gameId + "/pits/" + pitNum, "PUT", null, null, makeMoveFunction);
}

function makeAiMove() {
    sendHttpRequest("http://localhost:8080/games/" + gameId + "/ai", "PUT", null, null, makeMoveFunction);
}