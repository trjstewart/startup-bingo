// Packages and Includes
var fs = require('fs');
var mongoose = require('mongoose');
var validator = require('validator');

// Mongoose Models
var EmailOptin = require('../models/emailoptin.model');

module.exports = function(app, socket){
  app.post('/api/emailoptin', function(req, res){
    if (validator.isEmail(req.body.email.toString())) {
      var emailoptin = new EmailOptin({ email: req.body.email.toString() });
      emailoptin.save(function(err, emailoptin) {
        if(!err) res.send({result: true, message: 'Email Added Successfully'});
        else res.send({result: false, message: 'Email Address already added'});
      });
    } else {
      res.send({result: false, message: 'Invalid Email Address'});
    }
  });

  app.get('/api/words', function(req, res){
    var arrOfWords =[], nOfWords = 25;
    fs.readFile('database.json', 'utf8', function (err, data) {
      var obj = JSON.parse(data);
      if (err) res.send(err)
      for(var i=0;i<obj["word-list"].length;i++){
        arrOfWords.push(obj["word-list"][i].word)
      }
      var w = getWords(arrOfWords, nOfWords);
      res.send({status:200, data: w});
    });
  });
};

function getWords(arrOfWords, n){
  var chosenWords = [], length = arrOfWords.length, taken = [], temp =[];

  if(n>arrOfWords.length){
    throw new Error('fuck you doing son?');
  }

  while(n--){
    var x = Math.floor(Math.random()*length);
    chosenWords[n] = arrOfWords[x in taken ? taken[x] : x ]
    taken[x] = --length;
  }
  return chosenWords
}