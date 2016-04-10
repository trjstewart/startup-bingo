var mongoose = require('mongoose');
var Schema = mongoose.Schema;

emailoptinSchema = new Schema( {
    word: { type: String, unique: true },
    called: { type: Number, default: 0 }
});

EmailOptin = mongoose.model('emailoptin', emailoptinSchema);

module.exports = EmailOptin;