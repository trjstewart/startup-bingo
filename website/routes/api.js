var express = require('express');
var router = express.Router();

var EmailOptin = require('../models/emailoptin.model');

// Email Optin
router.post('/emailoptin', function(req, res) {
  console.log(req);
});

module.exports = router;
