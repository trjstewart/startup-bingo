var fs = require('fs');

module.exports = function(app, socket){


  app.get('/emailoptin', function(req, res){

    //TODO
    res.send({status:200,data:"1v1"})

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