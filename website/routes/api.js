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

  app.get('/words', function(req, res){
      fs.readFile('../database.json')
  });


};