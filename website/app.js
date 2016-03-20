var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var routes = require('./routes/index');
var api = require('./routes/api');

// Console.Slack (Thanks David <3)
var slack = require('console-slack');
slack.options = {
  webhook : "https://hooks.slack.com/services/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",
  username: "console.slack.bot", // the username you want to log with,
  emoji : ":nerd_face:", // set an emoji to be the bots profile picture,
  channel : "#startup-bingo",
};

// Import, Configure and Initialize Mongoose.
var mongoose = require('mongoose');
mongoose.connect('mongodb://startupbingo:segS3K4Zd0gz@ds043991.mlab.com:43991/startup-bingo', function(error) {
  (error) ? console.log('Database Connection Error: ' + error) : console.log('Successfully Connected to MongoLab!');
  console.slack('Server is up and Database has connected!');
});

//require('./models/emailoptin.model.js');

var app = express();
//var io = require('socket.io').listen(server);
//io.sockets.on('connection', function(socket) {
//  console.slack('Someone Created a Socket! Was it Jubb?');
//
//  socket.on('test', function () {
//    console.slack('Well it worked...');
//  });
//
//  socket.on('create', function(room) {
//    socket.join(room);
//  });
//});




// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Davids magic code to fix email bullshit
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

// Routes
app.use('/', routes);
//app.use('/api', api)(socket);
//require('./routes/api.js')(app, io);
require('./routes/api.js')(app);
// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
