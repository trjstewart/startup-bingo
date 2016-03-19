var mongoose = require('mongoose');
var Schema = mongoose.Schema;

emailpotinSchema = new Schema( {
    dateTime: { type : Date, default: Date.now },
    email: String
});

EmailOptin = mongoose.model('bump', emailpotinSchema);

module.exports = EmailOptin;