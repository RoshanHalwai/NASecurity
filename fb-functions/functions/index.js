'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');

/*Mapping Daily Service Firebase Keys with Notification value*/
const dailyServiceLookup = {};
dailyServiceLookup['cooks'] = "Cook";
dailyServiceLookup['maids'] = "Maid";
dailyServiceLookup['childDayCares'] = "Child Day Care";
dailyServiceLookup['dailyNewsPapers'] = "Daily Newspaper";
dailyServiceLookup['carBikeCleaners'] = "Car/Bike Cleaner";
dailyServiceLookup['drivers'] = "Driver";
dailyServiceLookup['laundries'] = "Laundry";
dailyServiceLookup['milkmen'] = "Milkman";
		
admin.initializeApp(functions.config().firebase);

//Notifications triggered when Guests either Enters or Leaves the User Society

exports.guestNotifications = functions.database.ref('/visitors/private/{visitorUID}/status')
.onWrite((change, context) => {
	
		const visitorUID = context.params.visitorUID;
		
		return admin.database().ref("/visitors").child("private").child(visitorUID).once('value').then(queryResult => {
			const guestName = queryResult.val().fullName;
			const status = queryResult.val().status;
			const inviterUID = queryResult.val().inviterUID;
			
			if(status.localeCompare("Not Entered") === 0)
				return null;
			
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

exports.dailyServiceNotification = functions.database.ref('/dailyServices/all/public/{dailyServiceType}/{dailyServiceUID}/status')
	.onWrite((change, context) => {
				
		const dailyServiceType = context.params.dailyServiceType;
		const dailyServiceUID = context.params.dailyServiceUID;
		const promises = [];
		
		return admin.database().ref("/dailyServices").child("all").child("public").child(dailyServiceType).child(dailyServiceUID).once('value').then(queryResult => {
			const status = queryResult.val().status;
			if(status.localeCompare("Not Entered") === 0)
				return null;
			
			return queryResult.forEach((userSnap) => {
				var userUID = userSnap.key;
				
				if(userUID.localeCompare("status") !== 0) {
				
					const userDataReference = admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
						const tokenId = queryResult.val().tokenId;
						const payload = {
							data: {
								message: "Your " + dailyServiceLookup[dailyServiceType] + " has " + status + " your society.",
								type: "Daily_Service_Notification"
							}
						};

						return admin.messaging().sendToDevice(tokenId, payload).then(result => {
							return console.log("Notification sent");	
					
						});
					});
					promises.push(userDataReference);
				}
			
			});
		
		}).then(()=> {
			return Promise.all(promises);
	});
	
});
	
	
//Notifications triggered when Cabs either Enters or Leaves the User Society

exports.cabNotifications = functions.database.ref('/cabs/private/{cabUID}/status')
.onWrite((change, context) => {
	const cabUID = context.params.cabUID;
	
	return admin.database().ref("/cabs").child("private").child(cabUID).once('value').then(queryResult => {
		const status = queryResult.val().status;
		const inviterUID = queryResult.val().inviterUID;
		
		if(status.localeCompare("Not Entered") === 0)
			return null;
		
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

exports.packageNotifications = functions.database.ref('/deliveries/private/{deliveryUID}/status')
.onWrite((change, context) => {
	const deliveryUID = context.params.deliveryUID;
	
	return admin.database().ref("/deliveries").child("private").child(deliveryUID).once('value').then(queryResult => {
		const status = queryResult.val().status;
		const reference = queryResult.val().reference;
		const inviterUID = queryResult.val().inviterUID;
		
		if(status.localeCompare("Not Entered") === 0)
			return null;
		
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

// Notifications triggered when User sends notification to Society Service

exports.societyServiceNotifications = functions.database.ref('/userData/private/{city}/{society}/{apartment}/{flat}/societyServiceNotifications/{societyServiceType}/{notificationUID}')
.onCreate((change, context) => {

	const notificationUID = context.params.notificationUID;

	return admin.database().ref("/societyServiceNotifications").child("all").child(notificationUID).once('value').then(queryResult => {
		const societyServiceType = queryResult.val().societyServiceType;
		const ownerUID = queryResult.val().userUID;
		const eventDate = queryResult.val().eventDate;
		const timeSlot = queryResult.val().timeSlot;

		return admin.database().ref('/users').child("private").child(ownerUID).child("personalDetails").once('value').then(queryResult => {
			
			const userFullName = queryResult.val().fullName;
			
			return admin.database().ref('/users').child("private").child(ownerUID).child("flatDetails").once('value').then(queryResult => {
				
				const apartmentName = queryResult.val().apartmentName;
				const flatNumber = queryResult.val().flatNumber;
				var mobileNumber;
				var tokenId;
				
				if (societyServiceType === "eventManagement"){
					
					console.log("Entered Event Management Block");
					
					return admin.database().ref('/societyServices').child("admin").once('value').then(queryResult =>{		
				
						tokenId = queryResult.val().tokenId;
						
						console.log("Society Service Type : " + societyServiceType);
						console.log("tokenId : " + tokenId);
						console.log("Event Date : " + eventDate);
						console.log("TimeSlot : " + timeSlot);
						
						const notificationMessage = userFullName +", "+ apartmentName +", "+ flatNumber +", "+" has requested for the hall, for "+ eventDate +" time slot "+ timeSlot +". Please confirm!";
						const payload = {		
								data: {
									message: notificationMessage,
									notificationUID: notificationUID,
									societyServiceType : societyServiceType,
									eventDate : eventDate,
									timeSlot : timeSlot
									}
								};
							
						return admin.messaging().sendToDevice(tokenId, payload).then(result => {
							return console.log("Notification sent");
						
					});
				
				});
					
				}else{
					const availableReference = admin.database().ref('/societyServices').child(societyServiceType).child("private")
					.child("available").on('value', function(snapshot){		

						console.log("Entered availableReference Block");
					
						var minimumServiceCount = -1;
						snapshot.forEach(function(child){
							
							const mobileReference = admin.database().ref('/societyServices').child(societyServiceType).child("private")
							.child("available").child(child.key).on('value', function(snapshot){
								var mobileNumberMap = snapshot.val();
								const serviceCount = mobileNumberMap["serviceCount"];
								
								if (minimumServiceCount === -1 || serviceCount < minimumServiceCount) {
									
									console.log("Entered IF Statement");
									
									minimumServiceCount = serviceCount;
									tokenId = mobileNumberMap["tokenId"];
									mobileNumber = child.key;
								}

							});
							
						});
						
						console.log("Society Service Type : " + societyServiceType);
						console.log("Mobile Number : " + mobileNumber);
						console.log("tokenId : " + tokenId);
						
						const notificationMessage = userFullName + " needs your service at " + apartmentName + " , " + flatNumber + ". Please confirm! ";
						const payload = {		
								data: {
									message: notificationMessage,
									notificationUID: notificationUID, 
									mobileNumber : mobileNumber,
									societyServiceType : societyServiceType
									}
								};
							
						return admin.messaging().sendToDevice(tokenId, payload).then(result => {
							return console.log("Notification sent");
						
					});
				
				});
				}
				
				return console.log("End Of Function");
			});
		});
	});
	
});
					

// Notifications triggered when User Raises an Emergency Alarm to Security Guard

exports.emergencyNotifications = functions.database.ref('/emergencies/public/{emergencyUID}')
.onCreate((change, context) => {
	
	const emergencyUID = context.params.emergencyUID;
	
	return admin.database().ref('/emergencies').child('public').child(emergencyUID).once('value').then(queryResult => {
		const ownerName = queryResult.val().fullName;
		const emergencyType = queryResult.val().emergencyType;
		const apartmentName = queryResult.val().apartmentName;
		const flatNumber = queryResult.val().flatNumber;
		const mobileNumber = queryResult.val().phoneNumber;
		
		console.log("Owners Name: "+ownerName);
		console.log("Emergency Type: "+emergencyType);
		console.log("Apartment Name: "+apartmentName);
		console.log("Flat Number: "+flatNumber);
		console.log("Mobile Number:"+mobileNumber);
		
		const guardReference = admin.database().ref('/societyServices').child("guard").child("private")
					.child("data").on('value', function(snapshot){		

						console.log("Entered guardReference Block");
					
						var tokenId;
						snapshot.forEach(function(child){
							
							const adminReference = admin.database().ref('/societyServices').child("guard").child("private")
							.child("data").child(child.key).on('value', function(snapshot){
								var guardUIDMap = snapshot.val();
								const isAdmin = guardUIDMap["admin"];
								
								if (isAdmin) {				
								console.log("Entered IF Statement");
								tokenId = guardUIDMap["tokenId"];									
								}

							});
							
						});						
						
						const payload = {		
								data: {
										message: "A " + emergencyType + " emergency has been raised by " + ownerName + " of " + flatNumber + " in " + apartmentName,
										emergencyType: emergencyType,
										ownerName: ownerName,
										mobileNumber: mobileNumber,
										apartmentName: apartmentName,
										flatNumber: flatNumber
									}
								};
							
						return admin.messaging().sendToDevice(tokenId, payload).then(result => {
							return console.log("Notification sent");
						
					});
				
		});
				
		return console.log("End of Function");
	});
	
});
	
//Notifications triggered when privilege value is set to true/false

exports.activateAccountNotification = functions.database.ref('/users/private/{userUID}/privileges/verified')
.onWrite((change, context) => {
	const userUID = context.params.userUID;
	
	return admin.database().ref("/users").child("private").child(userUID).child("privileges").once('value').then(queryResult => {
		const verified = queryResult.val().verified;
		
		if(verified === true) {
			return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const payload = {
						notification: {
							title: "Namma Apartments",
							body: "Welcome to Namma Apartments, Your Account has been Activated",
							"sound": "default",
							"badge": "1"
						},
					data: {
						message: "Welcome to Namma Apartments, Your Account has been Activated",
						type: "userAccountNotification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});				
				
			});
		} else {
			return admin.database().ref("/societyServices").child("admin").once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const payload = {
					data: {
						message: "A new User Account has been created. Requires Authentication",
						societyServiceType: "userAccountNotification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});				
				
			});
		}
	});
});

//Notifications triggered when Security Guard uses E-Intercom facility to ask permission from User

exports.sendNotifications = functions.database.ref('/userData/private/{city}/{society}/{apartment}/{flat}/gateNotifications/{userUID}/{visitorType}/{notificationUID}')
.onCreate((change,context)=>{
	const city = context.params.city;
	const society = context.params.society;
	const apartment = context.params.apartment;
	const flat = context.params.flat;
	const userUID = context.params.userUID;
	const notificationUID = context.params.notificationUID;
	const visitorType = context.params.visitorType;
	
	console.log("City:" + city);
	console.log("Society:" + society);
	console.log("Apartment:" + apartment);
	console.log("Flat:" + flat);
	console.log("UserUID:" + userUID);
	console.log("NotificationUID:" + notificationUID);
	
	return admin.database().ref("/userData").child("private")
	.child(city).child(society).child(apartment).child(flat)
	.child("gateNotifications").child(userUID).child(visitorType).child(notificationUID)
	.once('value').then(queryResult => {

		const message = queryResult.val().message;
		const profilePhoto = queryResult.val().profilePhoto;
		
		console.log("NotificationUID:" + profilePhoto);
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult=>{
			
			const tokenId = queryResult.val().tokenId;
			
			console.log("Token Id : " + tokenId);
			
			const payload = {
				data: {
					message: message,
					notification_uid : notificationUID,
					user_uid : userUID,
					visitor_type : visitorType,
					profile_photo : profilePhoto,
					type: "E-Intercom"
				}
            };
			
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
			
		});
		
		
	});

	
});