var fs = require('fs');
var Player = require('../libs/player.js');
var sanitizeHtml = require('sanitize-html'); //u cheeky bastards. god damn  hackers. shiiiieeeettt


var players = {};
var nOfWords = 16, winScore = nOfWords, maxMessageLength = 240;//winscore is still here because size of board is undecided of yet. we may require a center empty tile
var arrOfWords = [], usernames = [], defaultAllowedTags = ['blockquote', 'a','nl', 'b', 'i', 'strong', 'em', 'strike'];


/*
 set max character limit x2
 ban byron

 */
module.exports = function (io) {
    io.sockets.on('connection', function(socket) {
        //console.slack('Someone Created a Socket! Was it Jubb?');
        console.log('client id joined: ' + socket.id);
        socket.on('join_room', function(data) {
            try {
                data.toString();
                var room = data.hashtag;
                var username = data.username;
                (room.charAt(0) === '#') ? room = room.substr(1) : null;//why is this here? client side plz b0ss

                console.log('player ' + username + ' joined room ' + room);

                if(usernameIsUnique(username, room)){
                    socket.join(room);
                    players[socket.id] = new Player(username, room);
                    players[socket.id].sanitizerOptions.allowedTags = defaultAllowedTags;
                    socket.emit('join_room', {result: true});
                    io.sockets.in(room).emit('player-joined', username);
                }else{
                    // throw {name: InvalidUsername, message: "Username already exists"}
                    socket.emit('join_room', {result: false, err: "Invalid Username"})
                }

            } catch(err) {
                socket.emit('join_room', {result: false, err: err});
            }
        });

        //clean up arrays or players/usernames
        socket.on('disconnect', function(){
            if(players[socket.id]){
                usernames.splice(usernames.indexOf(players[socket.id].userName));
            }

            delete players[socket.id];
        });

        socket.on('message', function(message){
            if(message.length > maxMessageLength){
                io.sockets.in(players[socket.id].room).emit('message', {user: players[socket.id].userName, message:players[socket.id].userName + ' tried sending a very long message. Compensating huh?'});
            }else{
                //console.slack('FUCK BOII SENT U A MESSAGE');
                // var clean = sanitizeHtml(message, {
                //     allowedTags: sanitizeHtml.defaults.allowedTags.concat([ 'img' ])
                // });
                var clean = sanitizeHtml(message, {
                    allowedTags : players[socket.id].sanitizerOptions.allowedTags
                });

                console.log(clean);

                io.sockets.in(players[socket.id].room).emit('message', {user: players[socket.id].userName, message:clean});
            }

        });



        socket.on('word-found', function(word){
            if(!(players[socket.id].wordsFound.indexOf(word)>-1)){
                players[socket.id].wordsFound.push(word);

                io.sockets.to(players[socket.id].room).emit('player-found-word', {user: players[socket.id].userName, word: word});



                if(players[socket.id].wordsFound.length >= winScore){
                    console.log('WINNERRR');
                    players[socket.id].resetWordsFound();
                    players[socket.id].wins++;
                    //allow img tag after a win
                    if(players[socket.id].wins===1){
                        players[socket.id].imagesAllowed();
                    }
                    io.sockets.in(players[socket.id].room).emit('winner-found', {winner: players[socket.id].userName, wins: players[socket.id].wins});

                    //tell client to restart game;
                    io.to(socket.id).emit('restart',{status: 200, data:'restart game'});

                }
            }else{
                console.log('word already in');
            }
        });

        socket.on('words', function(){

            fs.readFile('database.json', 'utf8', function(err, data){
                if(err) console.error(err);
                var obj = JSON.parse(data);
                for(var i=0;i<obj["word-list"].length;i++){
                    arrOfWords.push(obj["word-list"][i].word)
                }
                var g = getWords(arrOfWords, nOfWords);
                players[socket.id].words = g;
                io.to(socket.id).emit('words',{status: 200, data:g});
            });

        });

        socket.on('get-scores', function(){
            io.to(socket.id).emit('get-scores',{status: 200, data:getLeadBoard()});
        });
    });
};

/*
 track individual
 track score
 unique username
 */

function getWords(arrOfWords, n){
    var shuffledArray = shuffle(arrOfWords);
    var t = [], item;

    for(var i=0;i<n;i++){
        do{
            item = arrOfWords[Math.floor(Math.random()*arrOfWords.length)];
        }while(t.indexOf(item) > -1);
        t.push(item)
    }
    return t;
}



//It works. I think. maybe.
function getLeadBoard(){
    var playerArray = Object.keys(players);
    var leaderBoard = [];

    for(var i=0;i<playerArray.length;i++){
        leaderBoard.push({username: players[playerArray[i]].userName, score: players[playerArray[i]].wins})
    }
    return leaderBoard;
}


//fisher-yates shuffle
function shuffle(array) {
    var input = array;

    for (var i = input.length-1; i >=0; i--) {

        var randomIndex = Math.floor(Math.random()*(i+1));
        var itemAtIndex = input[randomIndex];

        input[randomIndex] = input[i];
        input[i] = itemAtIndex;
    }
    return input;
}

//check usernames
function usernameIsUnique(uName, room){
    console.log(players);
    for (var key in players) {
        if (players.hasOwnProperty(key)) {
            if(players[key].userName === uName && players[key].room === room){
                console.log('return false')
                return false;
            }
        }
    }
    return true;

}

function processMessage(message){

}