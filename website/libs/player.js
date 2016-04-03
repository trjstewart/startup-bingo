
var Player = function(username){
    this.score = 0;
    this.wordsFound = [];
    this.words = [];
    this.userName = username;
    this.status = 'playing';
    this.wins = 0;
};

Player.prototype.resetWordsFound = function(){
  this.wordsFound = [];
};



module.exports = Player;

