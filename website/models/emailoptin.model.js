var mongoose = require('mongoose');
var Schema = mongoose.Schema;

emailoptinSchema = new Schema( {
    dateTime: { type : Date, default: Date.now },
    email: { type: String, unique: true }
});

EmailOptin = mongoose.model('emailoptin', emailoptinSchema);

module.exports = EmailOptin;