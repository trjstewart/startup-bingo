
var Player = function(username, room){
    this.score = 0;
    this.wordsFound = [];
    this.words = [];
    this.userName = username;
    this.status = 'playing';
    this.wins = 0;
    this.room = room;
    this.sanitizerOptions = {
        allowedTags : [],
        allowedAttributes : [],
        selfClosing : [],
        allowedSchemes: [ 'http', 'https', 'ftp', 'mailto' ]
    }

};

Player.prototype.resetWordsFound = function(){
  this.wordsFound = [];
};

Player.prototype.imagesAllowed = function(){
    if(this.sanitizerOptions.allowedTags.indexOf('img') === -1){
        this.sanitizerOptions.allowedTags.push('img');
    }
  
};



module.exports = Player;

