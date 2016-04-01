
var Player = function(username){
    this.score = 0;
    this.wordsFound = 0;
    this.userName = username;
    this.status = 'playing';
    this.wins = 0;
};

module.exports = Player;