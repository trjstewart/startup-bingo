var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var fs = require('fs');

var routes = require('./routes/index');
var users = require('./routes/users');

var Twitter = require('twitter');

var client = new Twitter({
  consumer_key: 'IL2yiItZIsOKgGHQAMtlMP3Zt',
  consumer_secret: '2Pg43U3FSW0qglQG1onoztW2hfDZrljmMeB8Vouwm7X6zFskUM',
  access_token_key: '220171198-ITFzZh7oFajnPjfrohAYyALnkIFC0j1wS9yXst6Q',
  access_token_secret: 'GSxilS1zSFW4JeHif7xXraZWmLIApln83GHBVZFb6YKFv'
});//dont steal plz b0ss

var app = express();

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

var trackedTopics = 'startup,startup weekend';

client.stream('statuses/filter', {track: trackedTopics}, function(stream) {
  stream.on('data', function(tweet) {
    fs.appendFile('user_List/userIds.json', tweet.user.id+'\n', function(err){
      if(err){console.log(err)}
    });
    console.log(tweet.text);
  });

  stream.on('error', function(error) {
    console.error(error);
  });
});



 function sendTweet(text, callback){

   client.post('statuses/update', {status: text}, function(error, tweet, response){
     if (!error) {
       console.log(tweet);
     }else{
       console.error(error);
     }

     if(typeof(callback)==='function'){
       callback();
     }

   });

 }









//app.use('/', routes);
//app.use('/users', users);

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

/*

                                                                     PRAISE 420 JESUS
 8888888888888888888888888888888888888888888888888888888888888888888Oo::           .:oO8888888888888888888888888888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888888888888888888C..      .......        ..C888888888888888888888888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888888888888888c. .......:::.:::::::... . .    :888888888888888888888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888888888888o  ...:.::::::c:ccc:c:::::.......     c888888888888888888888888888888888888888888888888888888
 O8OOOOC88888888888888888888C8O88888888888888888888888888. ..:::::::c:::cc:cccc:::..::......      :888888888888888O8OOOCC8888888888888888888CCO88888888
 8C888COo8888888888888888888C8O888888888888888888888888:...::::::::::ccc:ocooooccccc::::......      C888888888888OOO888CCO888888888888888888CO8O8888888
 OOC8OCOo8888888888888888888C888888888888888888888888O....:::::::ccco:ccocoooooCoooccc::::...:.....  C88888888888COCO8COCC8888888888888888O8CC8O8888888
 OCCCCCo88888888888888888O88o88O88888888888888888888O ...::::::ooooooooCCCCoCCCCCCCCooooooooc::....   C888888888C8CCCCCoO88888888888888888O8CC8O8888888
 C88888888888888888888888O88o88O88888888888888888888..::::oCCCOOOOOOO888O888O8OOOOOOOCCCCCCCCCoc::.....8888888OOCoO8888888888888888888888O88CC88O888888
 8888888888888888888888OC888o888C888888888888888888::ccoCOCOOOO88O888O88O88OOO8OOOOOOOCCCCCCCCCCCoc:....8888OOOoO88888888888888888888888C888CC888C88888
 88888888888888888888oooC88OC888Cooo8888888888888Coc:cCCOCOOOO8888888888888O888OOOOOOOCCCCCCCCCCCCCoc::.oO8OOocCo88888888888888888888Ocoo888CC888oocO88
 88888888888888888cccccoo88CCO88oocccco888888888COccooCOCOOOOO8O888888888888888OOOOCCCCCCCCCCCCCCCoooccc:CCooCC88888888888888888888:cccooC88CC88Cooccc:
 88888888888888888c:cccccc:C8o:ccccc:c:8888888888OcoCCCOCOOCOOO8O88O8O88888O888OOOCCCCCCCCCCCCCCCCCooooocO888888888888888888888888C:cccccc::88::cccc:c:
 888888888888888888888888888888888888888888888888CoCCOCCOCOOOOOOOOOOOO8OO888O8OOOOCOCCCoCCCCCoCCCCoCooocoC888888888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888CCCOCCCOOOOOOOOO88O8O8O888888O88888OOOOCOCCCCCCoCCoooCooO888888888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888CCCCCCOOOCOOOO888O88888O8888888OOCCoooooccooCCoooCoooooCO888888888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888COCOCOCCCCOCCCCoccccoCO8888888OOCoccc:::o:::ooooooCoooooC888888888888888888888888888888888888888888888
 8888888888888888888888888888888888888888888888888CCOCOCCCCocccococcoCCCO88888OOOCCooCCCCCCCCoccoooCoooooC888888888888888888888888888888888888888888888
 8O88888888888888888888888888C8OOOOC8888888888888OCCOCCCCooCCOOOCCCooCCCCOO888OCooCCooooooooCooocooooooooooc8888CC8888888888888888888888888888O8OOOOC88
 8O8888888888888888888888888O8CO88CCCO8888888888OCCCCCCCCoCCCooCooccccoCCO8888CCoCcooc:..:cc:oooooooooCooocC8888CO8O8888888888888888888888888OOC888CCo8
 888888888888888888888888888OOOC88COo888888888888oCCCOCCCCooc: c   .Co:cOOOOOOCoCc:oOc  .oC .ccooooCCooooCCO88O8OO8O8888888888888888888888888COCO8OOCC8
 88O88888888888888888888888C8OCCCCCo8888888888888OCCCOOCCCoc:cooCCOoooCOOCOOCCCoooCoCooococcooooCCoCCCoooCCO8O88CO88O88888888888888888888888COOCCCCoO88
 888O88888888888888888888C8OCo8888888888888888888OCCOOOOOOCCCCCCooCOO88OCCOOOCCoCoCOOCCoooooooCCCCCCCCCCoCC88888CC88O888888888888888888888COOoO88888888
 888o888888888888888888C8OCC888888888888888888888OCCOOOOOO8OOO888888888OOO8OOOCoCCCCOOOOOCOCCCCCCCCCCCCooOC8o888CC88OC888888888888888888C8OoO8888888888
 888CooC8888888888888C8OCCcCO88888888888888888888OoCCOOOO8O888OOO8888OOCOO88OOCooCCCCOOCOCCCOOCOOCCCCCCoccCoo888oC888ooo88888888888888OOCooCo8888888888
 88Oooccc:o888888888COCoooO8888888888888888888C:cocCCOOOOO888O8O8O8888OCOO8O88OOCooCOOOOCOCOCCOOOCCCCCooc.oooO88Co88Ccocccc888888888OCCocCo888888888888
 c:ccccc::c88888888888888888888888888888888888c:c:cCCCOOO8OO88O8O8O88OOOO88888OOOCCCCO8OOOOOOOCCCCCoCCooc.c:cc:c88::cccc:c:C888888888888888888888888888
 888888888888888888888888888888888888888888888888CcCCOOOOOOO88888888O8CccOO88OCCc:ocCoOOOOOOCCCCCCCCCooc..::::88888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888C.oCCCOOCOO8888888OO888OCooooccooCCCCCCO88OOOCCCCCoocc:....::o8888888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888C.:oCCCOOOCO88888OO888OCo:cooc..CCCCCocCOOOOCCCCooocc... .:.::::c8888888888888888888888888888888888888
 888888888888888888888888888888888888888888888888C..:ooCCCCOOO8OOOo8C88Oc.:..c:. ..coCoccCOCCCCoCooc:..   ...::::::cO8888888888888888888888888888888888
 8888888888888888888888888888888888888888888888888 ..:coCCCCOOOOCoOCcCc.  ..c:..  ...:c::COCCCoooc:.. .   ..::::.:::::o88888888888888888888888888888888
 O8OOOOC88888888888888888888COO88888888888888888C:    .::ooCCOOCo::::.:::cCO88OCCoccc.   .oOCoc::: .      ..:::....:::::cC888888888888888888CCO88888888
 8C888COo8888888888888888888C8O88888888888Oc:..... .  .. :cooCCc. :cccccccc::c::::::occo..cooc:.. .       ..::.  .....:::ccC8888888888888888OO8O8888888
 OOC8OOCo8888888888888888888C888888888o:..........      ..:.cccc..ooO8888O8OOOOCCOO8CCCo:.:c..            .:..   ......:::ccoO88888888888888CC8O8888888
 OCCCCoo88888888888888888O88o88O88C:.............           :: ...:O88888OCoCCoCCOCCOCCo:....             .... ..........::ccCO88888888888O8CC8O8888888
 C88888888888888888888888O88o88oc:..................          ... cCO8OOOCc::.ccoCCCCCoc:                .... ............::coCO888888888O88CC88O888888
 8888888888888888888888OC888oCc:.............:::::::            . :cCOOOCCoc: coooCCoooc                 .................:::cCO88888888C888CC88OC88888
 88888888888888888888CooC88Occ::.........::::::::::.              .ooOOCOCOocoCOCCoCOCC:                ................:.:::coC888888coo888CC888ooc888
 88888888888888888cccccoo88C:::.......:::::::::::::.               .coCcoc::oooCcccoc: .              ...............:.:.::::coC888:cccooC88CC88Cooccc:
 88888888888888888cc:ccccc:oo. .....:::::::::c:::::.                ...:::c.coo:o:::.               ................:::::::::coCO8C:c:cccc::88.:cccc:c:
 88888888888888888888888888Cc.....::::::::c::::::::..                  ..::...::   .  .          ..................:::::::::::cCO8888888888888888888888


 */