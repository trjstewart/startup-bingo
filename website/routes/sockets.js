var fs = require('fs');

module.exports = function (io) {
    io.sockets.on('connection', function(socket) {
        console.slack('Someone Created a Socket! Was it Jubb?');
        var id = socket.id;
        console.log('client id joined: ' + id);

        //io.to(id).emit('words', {words: ['R A R E', 'S C R A P E']});

        socket.on('test', function () { console.slack('Wait! Did that bush move? :O'); });

        socket.on('join_room', function(room) {
            try {
                room.toString();
                (room.charAt(0) === '#') ? room = room.substr(1) : null;
                socket.join(room);
                console.slack('Someone just joined room: #' + room);
                socket.emit('join_room', {result: true});
            } catch(err) {
                socket.emit('join_room', {result: false, err: err});
            }
        });

        socket.on('words', function(){
            var arrOfWords = [], nOfWords = 25;
            fs.readFile('database.json', 'utf8', function(err, data){
                if(err) console.error(err);
                var obj = JSON.parse(data);
                for(var i=0;i<obj["word-list"].length;i++){
                    arrOfWords.push(obj["word-list"][i].word)
                }
                io.to(id).emit({status: 200, data:getWords(arrOfWords,25)});
            })
        });

    });
};


/*
var arrOfWords =[], nOfWords = 25;
fs.readFile('database.json', 'utf8', function (err, data) {
    var obj = JSON.parse(data);
    if (err) res.send(err)
    for(var i=0;i<obj["word-list"].length;i++){
        arrOfWords.push(obj["word-list"][i].word)
    }
    var w = getWords(arrOfWords, nOfWords);
    res.send({status:200, data: w});
});*/

function getWords(arrOfWords, n){
    var chosenWords = [], length = arrOfWords.length, taken = [], temp =[];

    if(n>arrOfWords.length){
        throw new Error('fuck you doing son?');
    }

    while(n--){
        var x = Math.floor(Math.random()*length);
        //console.log(x);
        (n===12)? chosenWords[n] = 'S W A G  L O R D' : chosenWords[n] = arrOfWords[x in taken ? taken[x] : x ];

        //console.log(chosenWords[n]);
        taken[x] = --length;
    }
    return chosenWords
}