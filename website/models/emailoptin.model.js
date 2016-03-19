var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var emailoptinSchema = new Schema({
    timedate: { type: Date, default: Date.now },
    email: String
});

module.exports = mongoose.model('EmailOptin', emailoptinSchema);