var fs = require('fs');
var Player = require('../libs/player.js');
var sanitizeHtml = require('sanitize-html'); //u cheeky bastards. god damn  hackers. shiiiieeeettt


var players = {};
var nOfWords = 16, winScore = nOfWords;//winscore is still here because size of board is undecided of yet. we may require a center empty tile
var arrOfWords = [];

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
                socket.join(room);
                socket.room = room;

                console.log('player ' + username + ' joined room ' + room);

                players[socket.id] = new Player(username);
                socket.emit('join_room', {result: true});
                io.sockets.in(room).emit('player-joined', username);
            } catch(err) {
                socket.emit('join_room', {result: false, err: err});
            }
        });

        socket.on('disconnect', function(){
            delete players[socket.id];
            console.log(players);
        });

        socket.on('message', function(message){
            //console.slack('FUCK BOII SENT U A MESSAGE');
            var clean = sanitizeHtml(message, {
                allowedTags: sanitizeHtml.defaults.allowedTags.concat([ 'img' ])
            });
            io.sockets.in(socket.room).emit('message', {user: players[socket.id].userName, message:clean});
        });

        socket.on('word-found', function(word){
            if(!(players[socket.id].wordsFound.indexOf(word)>-1)){
                players[socket.id].wordsFound.push(word);

                io.sockets.to(socket.room).emit('player-found-word', {user: players[socket.id].userName, word: word});
                if(players[socket.id].wordsFound.length >= winScore){
                    console.log('WINNERRR');
                    players[socket.id].resetWordsFound();
                    players[socket.id].wins++;
                    io.sockets.in(socket.room).emit('winner-found', {winner: players[socket.id].userName, wins: players[socket.id].wins});

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
 unique username // nah fuk dat errybody named dankherb420 now
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



//will this work? WHO KNOWs?!!?!?!420
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