module.exports = function (io) {
    io.sockets.on('connection', function(socket) {
        console.slack('Someone Created a Socket! Was it Jubb?');

        socket.on('test', function () { console.slack('Wait! Did that bush move? :O'); });

        socket.on('joinroom', function(room) {
            room.toString();
            (room.charAt(0) === '#') ? room = room.substr(1) : null;
            socket.join(room);
            console.slack('Someone just joined room: #' + room)
        });
    });
};