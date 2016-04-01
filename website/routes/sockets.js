module.exports = function (io) {
    io.sockets.on('connection', function(socket) {
        console.slack('Someone Created a Socket! Was it Jubb?');
        var id = socket.id;
        console.log('client id joined: ' + id);

        io.to(id).emit('words', {words: ['R A R E', 'S C R A P E']});

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
    });
};