'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//Notifications triggered when Guests either Enters or Leaves the User Society

exports.guestNotifications = functions.database.ref('/visitors/preApprovedVisitors/{visitorUID}')
.onWrite((change, context) => {
		const visitorUID = context.params.visitorUID;
		
		return admin.database().ref("/visitors").child("preApprovedVisitors").child(visitorUID).once('value').then(queryResult => {
			const guestName = queryResult.val().fullName;
			const status = queryResult.val().status;
			const inviterUID = queryResult.val().inviterUID;
			
			if(status.localeCompare("Not Entered") === 0)
				return;
			
			return admin.database().ref("/users").child("private").child(inviterUID).once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const payload = {
					data: {
						message: "Your Guest " + guestName + " has " + status + " your society.",
						type: "Guest_Notification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});				
				
			});
			
		});
		
	});

//Notifications triggered when Daily Services either Enters or Leaves the User Society

exports.dailyServiceNotification = functions.database.ref('/dailyServices/all/public/{dailyServiceType}/{dailyServiceUID}')
	.onWrite((change, context) => {
		const dailyServiceType = context.params.dailyServiceType;
		const dailyServiceUID = context.params.dailyServiceUID;
		
		return admin.database().ref("/dailyServices").child("all").child("public").child(dailyServiceType).child(dailyServiceUID).once('value').then(queryResult => {
			const status = queryResult.val().status;
			if(status.localeCompare("Not Entered") === 0)
				return;
			
			return queryResult.forEach((userSnap) => {
				var userUID = userSnap.key;
				
				return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const payload = {
					data: {
						message: "Your " + dailyServiceType + " has " + status + " your society.",
						type: "Daily_Service_Notification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});				
			
			});
			
		});
		
	});
	
});
	
	
//Notifications triggered when Cabs either Enters or Leaves the User Society

exports.cabNotifications = functions.database.ref('/cabs/public/{cabUID}')
.onWrite((change, context) => {
	const cabUID = context.params.cabUID;
	
	return admin.database().ref("/cabs").child("public").child(cabUID).once('value').then(queryResult => {
		const status = queryResult.val().status;
		const inviterUID = queryResult.val().inviterUID;
		
		if(status.localeCompare("Not Entered") === 0)
			return;
		
		return admin.database().ref("/users").child("private").child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			const payload = {
				data: {
					message: "Your Cab has " + status + " your society.",
					type: "Cab_Notification"
				}
			};

			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});				
			
		});
		
	});
	
});

//Notifications triggered when Packages either Enters or Leaves the User Society

exports.packageNotifications = functions.database.ref('/deliveries/public/{deliveryUID}')
.onWrite((change, context) => {
	const deliveryUID = context.params.deliveryUID;
	
	return admin.database().ref("/deliveries").child("public").child(deliveryUID).once('value').then(queryResult => {
		const status = queryResult.val().status;
		const reference = queryResult.val().reference;
		const inviterUID = queryResult.val().inviterUID;
		
		if(status.localeCompare("Not Entered") === 0)
			return;
		
		return admin.database().ref("/users").child("private").child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			const payload = {
				data: {
					message: "Your Package from " + reference + " has " + status + " your society.",
					type: "Package_Notification"
				}
			};

			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});				
			
		});
		
	});
	
});
	
//Notifications triggered when Security Guard uses E-Intercom facility to ask permission from User

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
					user_uid : userUID,
					type: "E-Intercom"
				}
            };
			
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
			
		});
		
		
	});

	
});