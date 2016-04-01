var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/sockets', function(req, res, next){
  res.render('socket');
});

module.exports = router;
