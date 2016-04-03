
Lists the socket endpoints for startup bingo

join_room:
   same as before

   **player-joined** emit sends data when a new player joins


word-found:
  on client side send a string. The string will be the word a user has clicked or tapped.
  this event will then emit a **'player-found-word'** event to everyone in the same room.
  If the word-found leads to a winner it is announced to the room that player X has won (using **'winner-found'** event)
  After winner has been found, the players game gets reset via a **'reset'** event.

message:
	sent string containing message. message string will be sanatized and then sent to entire room using 'message' emit.

words:
	same as before. except i fixed the duplicate words thing

get-scores:
	returns array of objects containing player names and number of winds. object looks like so:
					{username: 'username', score: 420}


**I think thats all for now, but may add more shit later**