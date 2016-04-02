var fs = require('fs');
var Player = require('../libs/player.js');
var players = {};
var nOfWords = 25, winScore = 24;

module.exports = function (io) {
    io.sockets.on('connection', function(socket) {
        //console.slack('Someone Created a Socket! Was it Jubb?');
        console.log('client id joined: ' + socket.id);

        var arrOfWords = [];
        fs.readFile('database.json', 'utf8', function(err, data){
            if(err) console.error(err);
            var obj = JSON.parse(data);
            for(var i=0;i<obj["word-list"].length;i++){
                arrOfWords.push(obj["word-list"][i].word)
            }
            var g = getWords(arrOfWords, nOfWords);
            io.to(socket.id).emit('words',{status: 200, data:g});
        });

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
            } catch(err) {
                socket.emit('join_room', {result: false, err: err});
            }
        });



        socket.on('disconnect', function(){
           delete players[socket.id];
            console.log(players);
        });

        socket.on('word-found', function(word){
            if(!(players[socket.id].wordsFound.indexOf(word)>-1)){
                players[socket.id].wordsFound.push(word);

                io.sockets.in(socket.room).emit('player-found-word', {user: players[socket.id].userName, word: word});
                if(players[socket.id].wordsFound.length >= winScore){
                    players[socket.id].wins++;
                    io.sockets.in(socket.room).emit('oh-fuck', {winner: players[socket.id].userName, wins: players[socket.id].wins});
                }
            }else{
                console.log('word already in');
            }
        });





    });
};

/*
track individual
track score
unique username // nah fuk dat errybody named dankherb420 now
 */

function getWords(arrOfWords, n){
    var chosenWords = [], length = arrOfWords.length, taken = [], temp =[];

    if(n>arrOfWords.length){
        throw new Error('fuck you doing son?');
    }

    while(n--){
        var x = Math.floor(Math.random()*length);
        (n===12)? chosenWords[n] = 'S W A G  L O R D' : chosenWords[n] = arrOfWords[x in taken ? taken[x] : x ];
        taken[x] = --length;
    }
    return chosenWords
}

