'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const APP_NAME = "Namma Apartments";

/*Society Details*/
const DEFAULT_DB_URL = "https://nammaapartments-beta.firebaseio.com/"
const DEFAULT_INSTANCE_NAME = "nammaapartments-beta"

const AIR_FORCE_COLONY_DB_URL = "https://nammaapartments-beta-airforcecolony.firebaseio.com/"
const AIR_FORCE_COLONY_INSTANCE_NAME = "nammaapartments-beta-airforcecolony"

const BRIGADE_GATEWAY_DB_URL = "https://nammaapartments-beta-brigade.firebaseio.com/"
const BRIGADE_GATEWAY_INSTANCE_NAME = "nammaapartments-beta-brigade"

/*Mapping Daily Service Firebase Keys with Notification value*/
const dailyServiceLookup = {};
dailyServiceLookup['cooks'] = "Cook";
dailyServiceLookup['maids'] = "Maid";
dailyServiceLookup['childDayCares'] = "Child Day Care";
dailyServiceLookup['dailyNewspapers'] = "Daily Newspaper";
dailyServiceLookup['carBikeCleaners'] = "Car/Bike Cleaner";
dailyServiceLookup['drivers'] = "Driver";
dailyServiceLookup['laundries'] = "Laundry";
dailyServiceLookup['milkmen'] = "Milkman";

/*Mapping Society Service Firebase Keys with Notification value*/
const societyServiceLookup = {};
societyServiceLookup['plumber'] = "Plumber";
societyServiceLookup['carpenter'] = "Carpenter";
societyServiceLookup['electrician'] = "Electrician";
societyServiceLookup['garbageCollection'] = "Garbage Collection";

/*Firebase Keys*/
const FIREBASE_CHILD_ADMIN = "admin";
const FIREBASE_CHILD_ALL = "all";
const FIREBASE_CHILD_AVAILABLE = "available";
const FIREBASE_CHILD_DATA = "data";
const FIREBASE_CHILD_FLATDETAILS = "flatDetails";
const FIREBASE_CHILD_GATENOTIFICATIONS = "gateNotifications";
const FIREBASE_CHILD_NOTIFICATIONSOUND = "notificationSound";
const FIREBASE_CHILD_OTHERDETAILS = "otherDetails";
const FIREBASE_CHILD_PERSONALDETAILS = "personalDetails";
const FIREBASE_CHILD_PRIVATE = "private";
const FIREBASE_CHILD_TOKENID = "tokenId";
const FIREBASE_CHILD_VISITORS = "visitors";
const FIREBASE_CHILD_STATUS = "status";
const FIREBASE_CHILD_TAKEN_BY = "takenBy";

/*Firebase Values*/
const FIREBASE_VALUE_NOT_ENTERED = "Not Entered";

/*Firebase App Initialization - (DEV/BETA) */
const defaultInstance = admin.initializeApp(functions.config().firebase);

const airForceColonyConfig = Object.assign({}, functions.config().firebase)
airForceColonyConfig.databaseURL = AIR_FORCE_COLONY_DB_URL;
const airForceColonyInstance = admin.initializeApp(airForceColonyConfig, AIR_FORCE_COLONY_INSTANCE_NAME)

const brigadeGatewayConfig = Object.assign({}, functions.config().firebase)
brigadeGatewayConfig.databaseURL = BRIGADE_GATEWAY_DB_URL;
const brigadeGatewayInstance = admin.initializeApp(brigadeGatewayConfig, BRIGADE_GATEWAY_INSTANCE_NAME)

/*Updating Pending Dues every month*/

exports.updatePendingDues = functions.https.onRequest((req, res) => {
	return admin.database().ref('/users/private').once('value')
	  .then(userUID => {
	  	var userUIDList = [];
	  	userUID.forEach((userUIDSnapshot) => {
	  		userUIDList.push(userUIDSnapshot.key);
	  	});
	  	return userUIDList;
	  })
	   .then(userUIDList => {
	   	var userFlatDetails = [];
	   	userUIDList.forEach(function (userUID) {
	   		userFlatDetails.push(admin.database().ref('/users/private').child(userUID).child(FIREBASE_CHILD_FLATDETAILS).once('value'));
	   	});
	   	return Promise.all(userFlatDetails);
	   })
	   .then(usersFlatDetailsPromises => {
	   	usersFlatDetailsPromises.forEach(userFlatData => {
	   		var userFlatReference = admin.database().ref('/userData/private')
                .child(userFlatData.val().city)
                .child(userFlatData.val().societyName)
                .child(userFlatData.val().apartmentName)
                .child(userFlatData.val().flatNumber);
	   		return userFlatReference.once('value')
                .then(queryResult => {
                	const maintenanceCost = queryResult.val().maintenanceCost;
                	var date = new Date();
                	var month = ("0" + (date.getMonth() + 1)).slice(-2);
                	var year = date.getFullYear();
                	return userFlatReference.child('pendingDues').child(month + year).set(maintenanceCost);
                });
	   	});
	   	console.log("Pending Dues Update Completed..")
	   	return res.redirect(200);
	   })
	   .catch(error => { console.log("Update Error") });
});


// Notifications related to Namma Apartments App (where user's action is required)

//Notifications triggered when Security Guard uses E-Intercom facility to ask permission from User
exports.sendNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/userData/private/{city}/{society}/{apartment}/{flat}/gateNotifications/{userUID}/{visitorType}/{notificationUID}')
.onCreate((change, context) => {
	return sendNotifications(airForceColonyInstance, change, context);
});

exports.sendNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/userData/private/{city}/{society}/{apartment}/{flat}/gateNotifications/{userUID}/{visitorType}/{notificationUID}')
.onCreate((change, context) => {
	return sendNotifications(brigadeGatewayInstance, change, context);
});

function sendNotifications(instance, change, context) {
	const city = context.params.city;
	const society = context.params.society;
	const apartment = context.params.apartment;
	const flat = context.params.flat;
	const userUID = context.params.userUID;
	const notificationUID = context.params.notificationUID;
	const visitorType = context.params.visitorType;

	return admin.database(instance).ref("/userData").child(FIREBASE_CHILD_PRIVATE)
	.child(city).child(society).child(apartment).child(flat)
	.child(FIREBASE_CHILD_GATENOTIFICATIONS).child(userUID).child(visitorType).child(notificationUID)
	.once('value').then(queryResult => {
		const notificationSnapshot = queryResult.val();
		const message = notificationSnapshot.message;
		const profilePhoto = notificationSnapshot.profilePhoto;
		var mobileNumber;
		if (visitorType === "guests") {
			mobileNumber = notificationSnapshot.mobileNumber;
		} else {
			mobileNumber = "";
		}

		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult=> {
			const tokenId = queryResult.val().tokenId;
			const deviceType = queryResult.child(FIREBASE_CHILD_OTHERDETAILS).val().deviceType;
			if (deviceType === "android") {
				const payload = {
					data: {
						message: message,
						notification_uid: notificationUID,
						user_uid: userUID,
						visitor_type: visitorType,
						profile_photo: profilePhoto,
						mobile_number: mobileNumber,
						type: "E-Intercom"
					}
				};
				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});
			} else {
				const payload = {
					notification: {
						title: APP_NAME,
						body: message,
						"sound": "default",
						"badge": "1",
						"click_action": "actionCategory",
						"mutable_content": "true",
						"content-available": "1"
					},
					data: {
						message: message,
						notification_uid: notificationUID,
						user_uid: userUID,
						visitor_type: visitorType,
						profile_photo: profilePhoto,
						mobile_number: mobileNumber,
						type: "E-Intercom"
					}
				};
				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});
			}
		});
	});
}

// Nofications related to Namma Apartments App (where user's action is  not required)

//Notifications triggered when Guests either Enters or Leaves the Society
exports.guestNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/visitors/private/{visitorUID}/status')
.onWrite((change, context) => {
	return guestNotifications(airForceColonyInstance, change, context);
});

exports.guestNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/visitors/private/{visitorUID}/status')
.onWrite((change, context) => {
	return guestNotifications(brigadeGatewayInstance, change, context);
});

function guestNotifications(instance, change, context) {
	/*If record is deleted or if status is Not Entered then we don't want to send notification to users*/
	const status = change.after.val();
	if (status === null || status === FIREBASE_VALUE_NOT_ENTERED)
		return 0;

	const visitorUID = context.params.visitorUID;

	/*Guest has either Entered or Left the society*/
	return admin.database(instance).ref("/visitors").child(FIREBASE_CHILD_PRIVATE).child(visitorUID).once('value').then(queryResult => {
		const guestName = queryResult.val().fullName;
		const inviterUID = queryResult.val().inviterUID;

		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			const flatDetails = queryResult.child(FIREBASE_CHILD_FLATDETAILS).val();
			const userCity = flatDetails.city;
			const userSocietyName = flatDetails.societyName;
			const userApartmentName = flatDetails.apartmentName;
			const userFlatNumber = flatDetails.flatNumber;

			var payload;

			return admin.database(instance).ref("/userData").child(FIREBASE_CHILD_PRIVATE)
						.child(userCity).child(userSocietyName).child(userApartmentName).child(userFlatNumber).child(FIREBASE_CHILD_VISITORS).child(inviterUID).child(visitorUID)
						.once('value').then(queryResult => {
							const isVisitorAvailable = queryResult.val();
							if (!isVisitorAvailable) {
								return null;
							}

							const notificationMessage = "Your Guest " + guestName + " has " + status + " your society.";
							payload = {
								notification: {
									title: APP_NAME,
									body: notificationMessage,
									"sound": "default",
									"badge": "1"
								},
								data: {
									message: notificationMessage,
									"sound": "default",
									type: "Guest_Notification"
								}
							};

							return admin.messaging().sendToDevice(tokenId, payload).then(result => {
								return console.log("Notification sent");
							});

						});

		});

	});

}

//Notifications triggered when Daily Services either Enters or Leaves the User Society
exports.dailyServiceNotification_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/dailyServices/all/public/{dailyServiceType}/{dailyServiceUID}/status')
.onWrite((change, context) => {
	return dailyServiceNotification(airForceColonyInstance, change, context);
});

exports.dailyServiceNotification_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/dailyServices/all/public/{dailyServiceType}/{dailyServiceUID}/status')
.onWrite((change, context) => {
	return dailyServiceNotification(brigadeGatewayInstance, change, context);
});

function dailyServiceNotification(instance, change, context) {
		const status = change.after.val();
		if (status === null || status === FIREBASE_VALUE_NOT_ENTERED)
			return 0;

		const dailyServiceType = context.params.dailyServiceType;
		const dailyServiceUID = context.params.dailyServiceUID;
		const promises = [];

		return admin.database(instance).ref("/dailyServices").child(FIREBASE_CHILD_ALL).child("public").child(dailyServiceType).child(dailyServiceUID).once('value').then(queryResult => {

			return queryResult.forEach((userSnap) => {
				var userUID = userSnap.key;

				if (userUID.localeCompare("status") !== 0) {

					const userDataReference = admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult => {
						const tokenId = queryResult.val().tokenId;

						const flatDetailsSnapshot = queryResult.child(FIREBASE_CHILD_FLATDETAILS).val();
						const userSocietyName = flatDetailsSnapshot.societyName;
						const userCity = flatDetailsSnapshot.city;
						const userApartmentName = flatDetailsSnapshot.apartmentName;
						const userFlatNumber = flatDetailsSnapshot.flatNumber;

						var payload;
						return admin.database(instance).ref("/userData").child(FIREBASE_CHILD_PRIVATE)
							.child(userCity).child(userSocietyName).child(userApartmentName).child(userFlatNumber).child("dailyServices").child(dailyServiceType).child(dailyServiceUID)
							.once('value').then(queryResult => {

								const isDailyServiceWorking = queryResult.val();

								if (!isDailyServiceWorking) {
									return null;
								}

								const notificationMessage = "Your " + dailyServiceLookup[dailyServiceType] + " has " + status + " your society.";
								payload = {
									notification: {
										title: APP_NAME,
										body: notificationMessage,
										"sound": "default",
										"badge": "1"
									},
									data: {
										message: notificationMessage,
										"sound": "default",
										type: "Daily_Service_Notification"
									}
								};

								return admin.messaging().sendToDevice(tokenId, payload).then(result => {
									return console.log("Notification sent");
								});
							});
					});
					promises.push(userDataReference);
				}

			});

		}).then(() => {
			return Promise.all(promises);
		});

	}

//Notifications triggered when Admin adds a Notice
exports.noticeBoardNotification_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/noticeBoard/{noticeBoardUID}')
.onWrite((change, context) => {
	return noticeBoardNotification(airForceColonyInstance, change, context);
});

exports.noticeBoardNotification_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/noticeBoard/{noticeBoardUID}')
.onWrite((change, context) => {
	return noticeBoardNotification(brigadeGatewayInstance, change, context);
});

function noticeBoardNotification(instance, change, context) {

		const noticeBoardUID = context.params.noticeBoardUID;
		const promises = [];

		return admin.database(instance).ref("/noticeBoard").child(noticeBoardUID).once('value').then(queryResult => {
			const dateAndTime = queryResult.val().dateAndTime;
			const title = queryResult.val().title;

			return admin.database(instance).ref("users").child(FIREBASE_CHILD_PRIVATE).once('value').then(queryResult => {

				return queryResult.forEach((userSnap) => {
					var userUID = userSnap.key;

					const userDataReference = admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult => {
						const tokenId = queryResult.val().tokenId;
						const verified = queryResult.child("privileges").val().verified;

						const notificationMessage = "A notice has been added by your Society Admin. Please check your Notice Board.";
						if (verified === 1) {
							const payload = {
								data: {
									message: notificationMessage,
									type: "Notice_Board_Notification"
								},
								notification: {
									title: APP_NAME,
									body: notificationMessage,
									"sound": "default",
									"badge": "1"
								}
							};

							return admin.messaging().sendToDevice(tokenId, payload).then(result => {
								return console.log("Notification sent");
							});

						} else {
							return null;
						}
					});
					promises.push(userDataReference);

				});

			}).then(() => {
				return Promise.all(promises);

			});

		});

	}

//Notifications triggered when Cabs either Enters or Leaves the User Society

exports.cabNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/cabs/private/{cabUID}/status')
.onWrite((change, context) => {
	return cabNotifications(airForceColonyInstance, change, context);
});

exports.cabNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/cabs/private/{cabUID}/status')
.onWrite((change, context) => {
	return cabNotifications(brigadeGatewayInstance, change, context);
});

function cabNotifications(instance, change, context) {

	const status = change.after.val();
	if (status === null || status === FIREBASE_VALUE_NOT_ENTERED)
		return 0;

	const cabUID = context.params.cabUID;
	return admin.database(instance).ref("/cabs").child(FIREBASE_CHILD_PRIVATE).child(cabUID).once('value').then(queryResult => {
		const inviterUID = queryResult.val().inviterUID;

		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			var payload;

			const notificationMessage = "Your Cab has " + status + " your society.";
			payload = {
				notification: {
					title: APP_NAME,
					body: notificationMessage,
					"sound": "default",
					"badge": "1"
				},
				data: {
					message: notificationMessage,
					"sound": "default",
					type: "Cab_Notification"
				}
			};

			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});

		});

	});

}

//Notifications triggered when Packages either Enters or Leaves the User Society

exports.packageNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/deliveries/private/{deliveryUID}/status')
.onWrite((change, context) => {
	return packageNotifications(airForceColonyInstance, change, context);
});

exports.packageNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/deliveries/private/{deliveryUID}/status')
.onWrite((change, context) => {
	return packageNotifications(brigadeGatewayInstance, change, context);
});

function packageNotifications(instance, change, context) {

	const status = change.after.val();
	if (status === null || status === FIREBASE_VALUE_NOT_ENTERED)
		return 0;

	const deliveryUID = context.params.deliveryUID;

	return admin.database(instance).ref("/deliveries").child(FIREBASE_CHILD_PRIVATE).child(deliveryUID).once('value').then(queryResult => {
		const reference = queryResult.val().reference;
		const inviterUID = queryResult.val().inviterUID;

		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			var payload;

			const notificationMessage = "Your Package from " + reference + " has " + status + " your society.";
			payload = {
				notification: {
					title: APP_NAME,
					body: notificationMessage,
					"sound": "default",
					"badge": "1"
				},
				data: {
					message: notificationMessage,
					"sound": "default",
					type: "Package_Notification"
				}
			};

			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});

		});

	});

}

//Notifications triggered when privilege value is set to 0,1 or 2

exports.activateAccountNotification_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/users/private/{userUID}/privileges/verified')
.onWrite((change, context) => {
	return activateAccountNotification(airForceColonyInstance, change, context);
});

exports.activateAccountNotification_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/users/private/{userUID}/privileges/verified')
.onWrite((change, context) => {
	return activateAccountNotification(brigadeGatewayInstance, change, context);
});

function activateAccountNotification(instance, change, context) {

	const verified = change.after.val();
	if (verified === null)
		return 0;

	const userUID = context.params.userUID;
	return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).child("privileges").once('value').then(queryResult => {
		if (verified === 1 || verified === 2) {
			return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult => {

				var message;
				if (verified === 1) {
					message = "Welcome to Namma Apartments, Your Account has been Activated";
				} else {
					message = "Sorry, Your Account Activation has been rejected by Admin";
				}

				const tokenId = queryResult.val().tokenId;
				const payload = {
					notification: {
						title: APP_NAME,
						body: message,
						"sound": "default",
						"badge": "1"
					},
					data: {
						message: message,
						type: "userAccountNotification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});

			});
		} else {
			return admin.database(instance).ref("/societyServices").child(FIREBASE_CHILD_ADMIN).once('value').then(queryResult => {
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
}

// Notifications triggered when society service accepts User's Society Service request
exports.societyServiceResponseNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/societyServiceNotifications/all/{notificationUID}/takenBy')
.onWrite((change, context) => {
	return societyServiceResponseNotifications(airForceColonyInstance, change, context);
});

exports.societyServiceResponseNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/societyServiceNotifications/all/{notificationUID}/takenBy')
.onWrite((change, context) => {
	return societyServiceResponseNotifications(brigadeGatewayInstance, change, context);
});

function societyServiceResponseNotifications(instance, change, context) {

	const notificationUID = context.params.notificationUID;

	return admin.database(instance).ref("/societyServiceNotifications").child(FIREBASE_CHILD_ALL).child(notificationUID).once('value').then(queryResult => {
		const userUID = queryResult.val().userUID;
		const societyServiceType = queryResult.val().societyServiceType;

		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult => {

			const tokenId = queryResult.val().tokenId;

			const notificationMessage = "Your request for the " + societyServiceLookup[societyServiceType] + " Service has been accepted";
			const payload = {
				notification: {
					title: APP_NAME,
					body: notificationMessage,
					"sound": "default",
					"badge": "1"
				},
				data: {
					message: notificationMessage,
					type: "Society_Service"
				}
			};

			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});

		});
	});

}

// Notifications triggered when user sents message to another admin user of different flat

exports.receivedChatNotification_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/chats/private/{chatRoomUID}/{messageUID}')
.onCreate((change, context) => {
	console.log("receivedChatNotification_2 API Called");
	return receivedChatNotification(airForceColonyInstance, change, context);
});

exports.receivedChatNotification_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/chats/private/{chatRoomUID}/{messageUID}')
.onCreate((change, context) => {
	console.log("receivedChatNotification_2 API Called");
	return receivedChatNotification(brigadeGatewayInstance, change, context);
});

function receivedChatNotification(instance, change, context) {
	
	const chatRoomUID = context.params.chatRoomUID;
	const messageUID = context.params.messageUID;
	var senderUID;
	
	return admin.database(instance).ref("/chats").child(FIREBASE_CHILD_PRIVATE).child(chatRoomUID).child(messageUID).once('value').then(queryResult => {
		const receiverUID = queryResult.val().receiverUID;
		
		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(receiverUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			
			return admin.database(instance).ref("/chats").child(FIREBASE_CHILD_ALL).child(chatRoomUID).once('value').then(queryResult=>{
				
					const snapshotVal = queryResult.val();
					for(var UID in snapshotVal) {
						if(UID !== receiverUID) {
							senderUID = UID;
							console.log("Sender UID : " + senderUID);
						}
					}
					
					const notificationMessage = "A new message has been received";
					const payload = {
						notification: {
							title: APP_NAME,
							body: notificationMessage,
							"sound": "default",
							"badge": "1"
							},
						data: {
							message: notificationMessage,
							sender_uid: senderUID,
							chat_room_uid: chatRoomUID,
							type:"neighbour_chat"
						}
				};
			
				return admin.messaging().sendToDevice(tokenId,payload).then(result => {
					return console.log("Notification Sent");
				});
				
			});
			
		});
	});
}

// Nofications related to Namma Apartments Society Service App

// Notifications triggered when User sends notification to Society Service
exports.societyServiceNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/userData/private/{city}/{society}/{apartment}/{flat}/societyServiceNotifications/{societyServiceType}/{notificationUID}')
.onWrite((change, context) => {
	return societyServiceNotifications(airForceColonyInstance, change, context);
});

exports.societyServiceNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/userData/private/{city}/{society}/{apartment}/{flat}/societyServiceNotifications/{societyServiceType}/{notificationUID}')
.onWrite((change, context) => {
	return societyServiceNotifications(brigadeGatewayInstance, change, context);
});

function societyServiceNotifications(instance, change, context) {

	const notificationUID = context.params.notificationUID;

	return admin.database(instance).ref("/societyServiceNotifications").child(FIREBASE_CHILD_ALL).child(notificationUID).once('value').then(queryResult => {
		const societyServiceType = queryResult.val().societyServiceType;
		const ownerUID = queryResult.val().userUID;
		var eventDate = queryResult.val().eventDate;

		return admin.database(instance).ref('/users').child(FIREBASE_CHILD_PRIVATE).child(ownerUID).child(FIREBASE_CHILD_PERSONALDETAILS).once('value').then(queryResult => {

			const userFullName = queryResult.val().fullName;

			return admin.database(instance).ref('/users').child(FIREBASE_CHILD_PRIVATE).child(ownerUID).child(FIREBASE_CHILD_FLATDETAILS).once('value').then(queryResult => {

				const apartmentName = queryResult.val().apartmentName;
				const flatNumber = queryResult.val().flatNumber;
				var mobileNumber;
				var tokenId;

				if (societyServiceType === "eventManagement" || societyServiceType === "scrapCollection") {

					var notificationMessage;

					return admin.database(instance).ref('/societyServices').child(FIREBASE_CHILD_ADMIN).once('value').then(queryResult => {

						tokenId = queryResult.val().tokenId;

						if (societyServiceType === "eventManagement") {
							notificationMessage = userFullName + ", " + apartmentName + ", " + flatNumber + ", " + " has booked the hall, for an event on " + eventDate + ". ";
						} else {
							notificationMessage = userFullName + ", " + apartmentName + ", " + flatNumber + ", " + "wants to discard some Scrap items from the residence";
							eventDate = "";
						}

						const payload = {
							data: {
								message: notificationMessage,
								notificationUID: notificationUID,
								societyServiceType: societyServiceType,
								eventDate: eventDate
							}
						};

						return admin.messaging().sendToDevice(tokenId, payload).then(result => {
							return console.log("Notification sent");

						});

					});

				} else {

					/*
			        1. Function starts with collecting all Available Staff Details and sort them based
			            their service count.
			        2. Iterate over each of the staff and sends them User service request.
			        3. Since they receive a request we also increase their service count.
			        4. We give 15 seconds for the staff to respond to the request.
			        5. After 15 seconds we check the user notification (takenBy) property.
			        6. If it is present it indicates the staff has accepted the request, so we stop sending
			            request to other staffs.
			        7. Else we send request to next available staff and repeat step 3.
			        8. After iterating over all of the available staffs if the user notification (takenBy) property
			        is still not present, then it indicates none of the staff has accepted to user request.
			        9. We update User UI accordingly
			        */
					return admin.database(instance).ref('/societyServices').child(societyServiceType).child(FIREBASE_CHILD_PRIVATE).child(FIREBASE_CHILD_AVAILABLE).once('value')
					.then(availablePlumbersUID => {
						var availablePlumbersUIDList = [];
						availablePlumbersUID.forEach((plumberUIDSnapshot) => {
							availablePlumbersUIDList.push(plumberUIDSnapshot.key);
						});
						return availablePlumbersUIDList;
					})
					.then(availablePlumbersUIDList => {
						var availablePlumbers = [];
						availablePlumbersUIDList.forEach(function (plumberUID) {
							availablePlumbers.push(admin.database(instance).ref('/societyServices').child(societyServiceType).child(FIREBASE_CHILD_PRIVATE).child(FIREBASE_CHILD_DATA).child(plumberUID).once('value'));
						});
						return Promise.all(availablePlumbers);
					})
				    .then(availablePlumbersPromises => {
				    	var availablePlumbers = [];
				    	availablePlumbersPromises.forEach(promises => {
				    		availablePlumbers.push(promises.val());
				    	});
				    	availablePlumbers.sort((a, b) => parseFloat(a.serviceCount) - parseFloat(b.serviceCount));

				    	var i = 0, l = availablePlumbers.length;
				    	return (function iterator() {
							
							if(l === 0) {
								admin.database(instance).ref('/societyServiceNotifications')
									.child(FIREBASE_CHILD_ALL)
									.child(notificationUID)
									.child(FIREBASE_CHILD_STATUS)
									.set("Declined");
									return 0;
							}

				    		admin.database(instance).ref('/societyServiceNotifications')
                            .child(FIREBASE_CHILD_ALL)
                            .child(notificationUID)
                            .child(FIREBASE_CHILD_TAKEN_BY)
                            .once('value', function (snapshot) {
                            	if (snapshot.exists()) {
                            		console.log("Some Staff has taken this request, so we stop sending request to other staffs");
                            	} else {

                            		var tokenId = availablePlumbers[i].tokenId;
                            		var mobileNumber = availablePlumbers[i].mobileNumber;
                            		const notificationMessage = userFullName + " needs your service at " + apartmentName + " , " + flatNumber + ". Please confirm! ";
                            		const payload = {
                            			data: {
                            				message: notificationMessage,
                            				notificationUID: notificationUID,
                            				mobileNumber: mobileNumber,
                            				societyServiceType: societyServiceType
                            			}
                            		};

                            		//Update the service count of the staff to whom request is sent
                            		admin.database(instance).ref('/societyServices')
									.child(societyServiceType)
									.child(FIREBASE_CHILD_PRIVATE)
									.child(FIREBASE_CHILD_DATA)
									.child(availablePlumbers[i].uid)
									.child("serviceCount")
									.set(availablePlumbers[i].serviceCount + 1);

                            		admin.messaging().sendToDevice(tokenId, payload)
									.then(result => {
										
										if (++i < l) {
											setTimeout(iterator, 15000);
										} else{
											setTimeout(function(){
												admin.database(instance).ref('/societyServiceNotifications')
												.child(FIREBASE_CHILD_ALL)
												.child(notificationUID)
												.child(FIREBASE_CHILD_TAKEN_BY)
												.once('value', function (snapshot) {
														if(snapshot.exists()){
																console.log("Last staff member has taken this request");
														} else{
															admin.database(instance).ref('/societyServiceNotifications')
																.child(FIREBASE_CHILD_ALL)
																.child(notificationUID)
																.child(FIREBASE_CHILD_STATUS)
																.set("Declined");
														}
												});
											},15000);

										}
										return console.log("Notification sent");
									})
									.catch(error => { console.log("Notification Error") });

                            	}

                            	return 0;

                            });
							
				    	})();
						
				    });
				}
			});
		});
	});

}

// Notifications triggered when user cancels society service request
exports.userCancelsSocietyServiceRequestNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/societyServiceNotifications/all/{notificationUID}/status')
.onWrite((change, context) => {
	return userCancelsSocietyServiceRequestNotifications(airForceColonyInstance, change, context);
});

exports.userCancelsSocietyServiceRequestNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/societyServiceNotifications/all/{notificationUID}/status')
.onWrite((change, context) => {
	return userCancelsSocietyServiceRequestNotifications(brigadeGatewayInstance, change, context);
});

function userCancelsSocietyServiceRequestNotifications(instance, change, context){

	const status = change.after.val();
	if (status === null || status !== "Cancelled")
		return 0;

	const notificationUID = context.params.notificationUID;
	return admin.database(instance).ref("/societyServiceNotifications").child(FIREBASE_CHILD_ALL).child(notificationUID).once('value').then(queryResult => {
		const notificationSnap = queryResult.val();
		const societyServiceType = notificationSnap.societyServiceType;
		const societyServiceUID = notificationSnap.takenBy;

		return admin.database(instance).ref("/societyServices").child(societyServiceType).child(FIREBASE_CHILD_PRIVATE).child(FIREBASE_CHILD_DATA).child(societyServiceUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			var serviceCount = queryResult.val().serviceCount;
			
			//Decreasing Service Count of society service.
			admin.database(instance)
			.ref("/societyServices")
			.child(societyServiceType)
			.child(FIREBASE_CHILD_PRIVATE)
			.child(FIREBASE_CHILD_DATA)
			.child(societyServiceUID)
			.child("serviceCount")
			.set((serviceCount - 1));
			
			const payload = {
				data: {
					message: "Service has been Cancelled by the User",
					societyServiceType: "cancelledServiceRequest"
				}
			};
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
		});
	});
}

// Notifications triggered to Society Admin when User request to donate food 

exports.donateFoodNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/foodDonations/{foodDonationNotificationUID}/status')
.onWrite((change, context) => {
	return donateFoodNotifications(airForceColonyInstance, change, context);
});

exports.donateFoodNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/foodDonations/{foodDonationNotificationUID}/status')
.onWrite((change, context) => {
	return donateFoodNotifications(brigadeGatewayInstance, change, context);
});

function donateFoodNotifications(instance, change, context) {
	
	const status = change.after.val();
	if (status === null || status === "Collected")
		return 0;
	
	const foodDonationNotificationUID = context.params.foodDonationNotificationUID;

	return admin.database(instance).ref("/foodDonations").child(foodDonationNotificationUID).once('value').then(queryResult => {
		const userUID = queryResult.val().userUID;

		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult => {
			const userFullName = queryResult.child(FIREBASE_CHILD_PERSONALDETAILS).val().fullName;
			const userApartmentName = queryResult.child(FIREBASE_CHILD_FLATDETAILS).val().apartmentName;
			const userFlatNumber = queryResult.child(FIREBASE_CHILD_FLATDETAILS).val().flatNumber;

			return admin.database(instance).ref("/societyServices").child(FIREBASE_CHILD_ADMIN).once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const payload = {
					data: {
						message: userFullName + ", " + userFlatNumber + ", " + userApartmentName + ", wants to donate Food to the needy one's",
						societyServiceType: "userDonateFoodNotification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});
			});
		});

	});
}

// Notifications triggered to Society Admin when User raises 'Support' request 
exports.supportNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/support/{supportUID}')
.onWrite((change, context) => {
	return supportNotifications(airForceColonyInstance, change, context);
});

exports.supportNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/support/{supportUID}')
.onWrite((change, context) => {
	return supportNotifications(brigadeGatewayInstance, change, context);
});

function supportNotifications(instance, change, context) {
	const supportUID = context.params.supportUID;

	return admin.database(instance).ref("/support").child(supportUID).once('value').then(queryResult => {
		const userUID = queryResult.val().userUID;
		return admin.database(instance).ref("/users").child(FIREBASE_CHILD_PRIVATE).child(userUID).once('value').then(queryResult => {
			const userFullName = queryResult.child(FIREBASE_CHILD_PERSONALDETAILS).val().fullName;
			const userApartmentName = queryResult.child(FIREBASE_CHILD_FLATDETAILS).val().apartmentName;
			const userFlatNumber = queryResult.child(FIREBASE_CHILD_FLATDETAILS).val().flatNumber;

			return admin.database(instance).ref("/societyServices").child(FIREBASE_CHILD_ADMIN).once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const payload = {
					data: {
						message: userFullName + ", " + userFlatNumber + ", " + userApartmentName + ", has a raised a Support request" + ". ",
						societyServiceType: "supportNotification"
					}
				};

				return admin.messaging().sendToDevice(tokenId, payload).then(result => {
					return console.log("Notification sent");
				});
			});
		});

	});
}

// Nofications related to Namma Apartments Security App

// Notifications triggered when User Raises an Emergency Alarm. The Security Guard Admin and the Society Admin gets notified.
exports.emergencyNotifications_2 = functions.database.instance(AIR_FORCE_COLONY_INSTANCE_NAME).ref('/emergencies/public/{emergencyUID}')
.onWrite((change, context) => {
	return emergencyNotifications(airForceColonyInstance, change, context);
});

exports.emergencyNotifications_brigade = functions.database.instance(BRIGADE_GATEWAY_INSTANCE_NAME).ref('/emergencies/public/{emergencyUID}')
.onWrite((change, context) => {
	return emergencyNotifications(brigadeGatewayInstance, change, context);
});

function emergencyNotifications(instance, change, context) {

	const emergencyUID = context.params.emergencyUID;
	return admin.database(instance).ref('/emergencies').child('public').child(emergencyUID).once('value').then(queryResult => {
		const emergencySnapshot = queryResult.val();
		const ownerName = emergencySnapshot.fullName;
		const emergencyType = emergencySnapshot.emergencyType;
		const apartmentName = emergencySnapshot.apartmentName;
		const flatNumber = emergencySnapshot.flatNumber;
		const mobileNumber = emergencySnapshot.phoneNumber;

		const guardAdminReference = admin.database(instance).ref('/guards').child(FIREBASE_CHILD_PRIVATE)
            .child(FIREBASE_CHILD_ADMIN).on('value', function (snapshot) {
            	const adminGuardUID = snapshot.val();
            	const adminReference = admin.database(instance).ref('/guards').child(FIREBASE_CHILD_PRIVATE)
                    .child(FIREBASE_CHILD_DATA).child(adminGuardUID).child(FIREBASE_CHILD_TOKENID).on('value', function (snapshot) {
                    	const guardTokenId = snapshot.val();

                    	return admin.database(instance).ref("/societyServices").child(FIREBASE_CHILD_ADMIN).once('value').then(queryResult => {
                    		const adminTokenId = queryResult.val().tokenId;

                    		const payload = {
                    			data: {
                    				message: "A " + emergencyType + " emergency has been raised by " + ownerName + " of " + flatNumber + " in " + apartmentName,
                    				emergencyType: emergencyType,
                    				ownerName: ownerName,
                    				mobileNumber: mobileNumber,
                    				apartmentName: apartmentName,
                    				flatNumber: flatNumber,
                    				societyServiceType: "emergency"
                    			}
                    		};

                    		var tokenId = [guardTokenId, adminTokenId];
                    		return admin.messaging().sendToDevice(tokenId, payload).then(result => {
                    			return console.log("Notification sent");
                    		});
                    	});
                    });
            });
		return console.log("End of Function");
	});

}