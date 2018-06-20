# Kalah
My Kalah Game
Adam Galmor

A RESTful web application facilitating the playing of the game of Kalah.

In addition to the provided instructions, I have added three extra features:
1) The player can choose to play any of Kalah-3, Kalah-4, Kalah-5 or Kalah-6.
2) An AI for the human player to play with which can be set to either Player 1 or Player 2.
3) A simple UI to make the playing experience more pleasant.

# Instructions

Before running the server for the first time, please input the command:

WINDOWS - mvnw.cmd clean install
LINUX - ./mvnw clean install

To run the server, input the command:

WINDOWS - mvnw.cmd spring-boot:run
LINUX - ./mvwn spring-boot:run

The application is set to run on port 8080.

To create a game, a POST request with Content-Type application/json must be sent to the server containing the following info:

  numStonesPerPit - The initial number of stones per pit for each player. Must be between 3 and 6.
  difficulty - The difficulty setting for the AI. Must be between 1 (easy) and 3 (hard).
  
Limitation - The presence or lack thereof of the AI player is not kept server side. It is up to the player to request an AI move when appropriate. When playing through the UI, this is handled for the player automatically.

Example request:
{
	"numStonesPerPit": 4,
	"difficulty": 2
}

The server will respond with the following response:

  id - A unique UUID for the game
  uri - The URI to send requests for this specific game
  
Example response:
{
    "id": "942f4176-7233-4be3-b05d-60f3e2fb5a86",
    "uri": "http://10.88.111.2:8080/games/942f4176-7233-4be3-b05d-60f3e2fb5a86"
}

The game will now expect move requests. These are PUT requests as described in the instructions.

To make a move for a human player, the request is of the format:

  "http://localhost:8080/games/{gameId}/pits/{pitNum}"
  
To request the AI to make a move, the request is of the format:

  "http://localhost:8080/games/{gameId}/ai"

The response for such a request contains the following fields:

  id - The ID of the game
  uri - The URI the request was sent to
  status - A map of the current board
  message - Any extra info, such a win notification
  nextPlayer - Indicating who the next move belongs to (used by UI)
  
Example response - 
{
    "id": "942f4176-7233-4be3-b05d-60f3e2fb5a86",
    "uri": "http://10.88.111.2:8080/games/942f4176-7233-4be3-b05d-60f3e2fb5a86/pits/3",
    "status": {
        "1": 4,
        "2": 4,
        "3": 0,
        "4": 5,
        "5": 5,
        "6": 5,
        "7": 1,
        "8": 4,
        "9": 4,
        "10": 4,
        "11": 4,
        "12": 4,
        "13": 4,
        "14": 0
    },
    "message": "It is now Player 1's turn",
    "nextPlayer": "Player 1"
}


The UI - 

A simple, self-explanatory UI with controls to set and play the game. To create a game the number of stones, AI player and its difficulty (if applicable) must be chosen, followed by clicking the "Start Game" button.
To make a move, simply click on the desired pit.

Limitation - In this simple implementation, the UI does not save the current state of the game, and thus refreshing the window will cause a loss of information.

The AI - 

An AI to play against a human player built using an implementation of the Monte Carlo Tree Search algorithm - https://en.wikipedia.org/wiki/Monte_Carlo_tree_search
The difficulty setting determines how much time the AI spends each round determining its move.


