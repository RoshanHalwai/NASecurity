'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.sendNotifications = functions.database.ref('/userData/private/{city}/{society}/{apartment}/{flat}/notifications/{userUID}/{notification_id}')
.onCreate((change,context)=>{
	const city = context.params.city;
	const society = context.params.society;
	const apartment = context.params.apartment;
	const flat = context.params.flat;
	const userUID = context.params.userUID;
	const notification_id = context.params.notification_id;
	
	console.log("City:" + city);
	console.log("Society:" + society);
	console.log("Apartment:" + apartment);
	console.log("Flat:" + flat);
	console.log("UserUID:" + userUID);
	console.log("NotificationUID:" + notification_id);
	
	return admin.database().ref("/userData").child("private").child(city).child(society).child(apartment).child(flat).child("notifications").child(userUID).child(notification_id).once('value').then(queryResult => {

		const uid = queryResult.val().uid;
		const message = queryResult.val().message;
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult=>{
			
			const tokenId = queryResult.val().tokenId;
			
			console.log("Token Id : " + tokenId);
			
			const payload = {
				data: {
					message: message,
					notification_uid : uid,
					user_uid : userUID
				}
            };
			
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
			
		});
		
		
	});

	
});