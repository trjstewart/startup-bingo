var fs = require('fs');

module.exports = function(app, socket){


  app.get('/emailoptin', function(req, res){

    //TODO
    res.send({status:200,data:"1v1"})

  });

  app.get('/words', function(req, res){
      fs.readFile('../database.json')
  });


};
