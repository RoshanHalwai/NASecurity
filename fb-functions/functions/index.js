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

/*Mapping Society Service Firebase Keys with Notification value*/
const societyServiceLookup = {};
societyServiceLookup['plumber'] = "Plumber";
societyServiceLookup['carpenter'] = "Carpenter";
societyServiceLookup['electrician'] = "Electrician";
societyServiceLookup['garbageCollection'] = "Garbage Collection";
		
admin.initializeApp(functions.config().firebase);

//Notifications triggered when Guests either Enters or Leaves the User Society

exports.guestNotifications = functions.database.ref('/visitors/private/{visitorUID}/status')
.onWrite((change, context) => {
	
		const visitorUID = context.params.visitorUID;
		
		return admin.database().ref("/visitors").child("private").child(visitorUID).once('value').then(queryResult => {

		    /*Exit the API if snapshot does not exist, more likely Delete Operation has been triggered*/
            if ( ! queryResult.exists()) {
                return 0;
            }

			const guestName = queryResult.val().fullName;
			const status = queryResult.val().status;
			const inviterUID = queryResult.val().inviterUID;
			
			if(status.localeCompare("Not Entered") === 0)
				return null;
			
			return admin.database().ref("/users").child("private").child(inviterUID).once('value').then(queryResult => {
				const tokenId = queryResult.val().tokenId;
				const guestNotificationSound = queryResult.child("otherDetails").child("notificationSound").val().guest;
				var payload;
				
				if (guestNotificationSound) {
					payload = {
						notification: {
									title: "Namma Apartments",
									body: "Your Guest " + guestName + " has " + status + " your society.",
									"sound": "default",
									"badge": "1" 
						},
						data: {
							message: "Your Guest " + guestName + " has " + status + " your society.",
							"sound": "default",
							type: "Guest_Notification"
						}
					};
				} else {
					payload = {
						notification: {
									title: "Namma Apartments",
									body: "Your Guest " + guestName + " has " + status + " your society.",
									"sound": "",
									"badge": "1" 
						},
						data: {
							message: "Your Guest " + guestName + " has " + status + " your society.",
							"sound": "",
							type: "Guest_Notification"
						}
					};
				}
		
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

			/*Exit the API if snapshot does not exist, more likely Delete Operation has been triggered*/
            if ( ! queryResult.exists()) {
                return 0;
            }

			const status = queryResult.val().status;
			if(status.localeCompare("Not Entered") === 0)
				return null;
			
			return queryResult.forEach((userSnap) => {
				var userUID = userSnap.key;
				
				if(userUID.localeCompare("status") !== 0) {
				
					const userDataReference = admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
						const tokenId = queryResult.val().tokenId;
						const dailyServiceNotificationSound = queryResult.child("otherDetails").child("notificationSound").val().dailyService;
						var payload;
						
						if (dailyServiceNotificationSound) {
							payload = {
								notification: {
									title: "Namma Apartments",
									body: "Your " + dailyServiceLookup[dailyServiceType] + " has " + status + " your society.",
									"sound": "default",
									"badge": "1" 
								},
								data: {
									message: "Your " + dailyServiceLookup[dailyServiceType] + " has " + status + " your society.",
									"sound": "default",
									type: "Daily_Service_Notification"
								}
							};
						}
						else {
							payload = {
								notification: {
									title: "Namma Apartments",
									body: "Your " + dailyServiceLookup[dailyServiceType] + " has " + status + " your society.",
									"sound": "",
									"badge": "1" 
								},
								data: {
									message: "Your " + dailyServiceLookup[dailyServiceType] + " has " + status + " your society.",
									"sound": "",
									type: "Daily_Service_Notification"
								}
							};
						}
						

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

//Notifications triggered when Admin adds a Notice

exports.noticeBoardNotification = functions.database.ref('/noticeBoard/{noticeBoardUID}')
	.onCreate((change, context) => {
		
		const noticeBoardUID = context.params.noticeBoardUID;
		console.log("Notice Board UID is:" + noticeBoardUID);
		const promises = [];
		
		return admin.database().ref("/noticeBoard").child(noticeBoardUID).once('value').then(queryResult => {
			const dateAndTime = queryResult.val().dateAndTime;
			const title = queryResult.val().title;	
			console.log("Title is:" + title);
			
			return admin.database().ref("users").child("private").once('value').then(queryResult => {
		
			return queryResult.forEach((userSnap) => {
				var userUID = userSnap.key;
				console.log("User is:" + userUID);
				
				const userDataReference = admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
						const tokenId = queryResult.val().tokenId;
						const verified = queryResult.child("privileges").val().verified;
						console.log("Verified Value is -> "+ verified);
						
						if(verified === 1){
						
							const payload = {
								data: {
									message: "A notice has been added by your Society Admin. Please check your Notice Board.",
									type: "Notice_Board_Notification"
								},
								notification: {
									title: "Namma Apartments",
									body: "A notice has been added by your Society Admin. Please check your Notice Board.",
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
			
			}).then(()=> {
				return Promise.all(promises);
			
		});
		
	});
	
});
	
	
//Notifications triggered when Cabs either Enters or Leaves the User Society

exports.cabNotifications = functions.database.ref('/cabs/private/{cabUID}/status')
.onWrite((change, context) => {
	const cabUID = context.params.cabUID;

	return admin.database().ref("/cabs").child("private").child(cabUID).once('value').then(queryResult => {

	    /*Exit the API if snapshot does not exist, more likely Delete Operation has been triggered*/
        if ( ! queryResult.exists()) {
            return 0;
        }

		const status = queryResult.val().status;
		const inviterUID = queryResult.val().inviterUID;
		
		if(status.localeCompare("Not Entered") === 0)
			return null;
		
		return admin.database().ref("/users").child("private").child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			const cabNotificationSound = queryResult.child("otherDetails").child("notificationSound").val().cab;
			console.log("Cab Notification Sound is: ", cabNotificationSound);
			var payload;
			
			if (cabNotificationSound) {
				payload = {
					notification: {
									title: "Namma Apartments",
									body: "Your Cab has " + status + " your society.",
									"sound": "default",
									"badge": "1"
					},
					data: {
						message: "Your Cab has " + status + " your society.",
						"sound": "default",
						type: "Cab_Notification"
					}
				};
			}
			else {
				payload = {
					notification: {
									title: "Namma Apartments",
									body: "Your Cab has " + status + " your society.",
									"sound": "",
									"badge": "1"
					},
					data: {
						message: "Your Cab has " + status + " your society.",
						"sound": "",
						type: "Cab_Notification"
					}
				};
			}
			
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

	    /*Exit the API if snapshot does not exist, more likely Delete Operation has been triggered*/
	    if ( ! queryResult.exists()) {
	        return 0;
	    }

		const status = queryResult.val().status;
		const reference = queryResult.val().reference;
		const inviterUID = queryResult.val().inviterUID;
		
		if(status.localeCompare("Not Entered") === 0)
			return null;
		
		return admin.database().ref("/users").child("private").child(inviterUID).once('value').then(queryResult => {
			const tokenId = queryResult.val().tokenId;
			const packageNotificationSound = queryResult.child("otherDetails").child("notificationSound").val().package;
			var payload;
			
			if (packageNotificationSound) {
				payload = {
					notification: {
									title: "Namma Apartments",
									body: "Your Package has " + status + " your society.",
									"sound": "default",
									"badge": "1"
					},
					data: {
						message: "Your Package from " + reference + " has " + status + " your society.",
						"sound": "default",
						type: "Package_Notification"
					}
				};
				
			} else {
				payload = {
					notification: {
									title: "Namma Apartments",
									body: "Your Package has " + status + " your society.",
									"sound": "",
									"badge": "1"
					},
					data: {
						message: "Your Package from " + reference + " has " + status + " your society.",
						"sound": "",
						type: "Package_Notification"
					}
				};
				
			}
		
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
			        9. We update User UI accordingly (//TODO)
			        */
					return admin.database().ref('/societyServices').child(societyServiceType).child("private").child("available").once('value')
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
                                    availablePlumbers.push(admin.database().ref('/societyServices').child(societyServiceType).child("private").child("data").child(plumberUID).once('value'));
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

				            admin.database().ref('/societyServiceNotifications')
                            .child("all")
                            .child(notificationUID)
                            .child("takenBy")
                            .once('value', function(snapshot) {
                              if (snapshot.exists()) {
                                console.log("Some Staff has taken this request, so we stop sending request to other staffs");
                              } else {
                                console.log("None of the staff has taken this request, so we start sending request to other staffs");

                                console.log(availablePlumbers[i]);

                                var tokenId = availablePlumbers[i].tokenId;
                                var mobileNumber = availablePlumbers[i].mobileNumber;
                                const notificationMessage = userFullName + " needs your service at " + apartmentName + " , " + flatNumber + ". Please confirm! ";
                                const payload = {
                                        data: {
                                            message: notificationMessage,
                                            notificationUID: notificationUID,
                                            mobileNumber : mobileNumber,
                                            societyServiceType : societyServiceType
                                            }
                                        };

                                //Update the service count of the staff to whom request is sent
                                admin.database().ref('/societyServices')
                                .child(societyServiceType)
                                .child("private")
                                .child("data")
                                .child(availablePlumbers[i].uid)
                                .child("serviceCount")
                                .set(availablePlumbers[i].serviceCount + 1);

                                admin.messaging().sendToDevice(tokenId, payload)
                                .then(result => {
                                    return console.log("Notification sent");
                                })
                                .catch(error => {console.log("Notification Error")});

                              }

                              if(++i<l) {
                                  setTimeout(iterator, 15000);
                              }

                              return 0;

                            });

                        })();

				    });
				}
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

		const guardAdminReference = admin.database().ref('/guards').child("private")
            .child("admin").on('value', function(snapshot){
                const adminGuardUID = snapshot.val();

                const adminReference = admin.database().ref('/guards').child("private")
                    .child("data").child(adminGuardUID).child("tokenId").on('value', function(snapshot){
                        const tokenId = snapshot.val();

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

		});
		return console.log("End of Function");
	});
	
});

//Notifications triggered when privilege value is set to 0,1 or 2

exports.activateAccountNotification = functions.database.ref('/users/private/{userUID}/privileges/verified')
.onWrite((change, context) => {
	const userUID = context.params.userUID;

	return admin.database().ref("/users").child("private").child(userUID).child("privileges").once('value').then(queryResult => {

        /*Exit the API if snapshot does not exist, more likely Delete Operation has been triggered*/
	    if( ! queryResult.exists()) {
	        return 0;
	    }

		const verified = queryResult.val().verified;
		if(verified === 1 || verified === 2) {
			return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {

				var message;
				if (verified === 1) {
				    message = "Welcome to Namma Apartments, Your Account has been Activated";
				} else {
				    message = "Sorry, Your Account Activation has been rejected by Admin";
				}

				const tokenId = queryResult.val().tokenId;
				const payload = {
						notification: {
							title: "Namma Apartments",
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
		var mobileNumber;
		
			if(visitorType === "guests"){
				mobileNumber = queryResult.val().mobileNumber;
			} else {
				mobileNumber = "";
			}			
		
		console.log("NotificationUID:" + profilePhoto);
		console.log("Visitor's Mobile Number:" + mobileNumber);
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult=>{
			
			const tokenId = queryResult.val().tokenId;
			
			console.log("Token Id : " + tokenId);
			
			const payload = {
				notification: {
									title: "Namma Apartments",
									body: message,
									"sound": "default",
									"badge": "1",
									"click_action": "actionCategory"
				},
				data: {
					message: message,
					notification_uid : notificationUID,
					user_uid : userUID,
					visitor_type : visitorType,
					profile_photo : profilePhoto,
					mobile_number : mobileNumber,
					type: "E-Intercom"
				}
            };
			
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
			
		});
		
		
	});

	
});

// Notifications triggered when Society Admin responds to User's Event Request

exports.eventNotifications = functions.database.ref('/societyServiceNotifications/eventManagement/{notificationUID}')
.onUpdate((change, context) => {
	
	const notificationUID = context.params.notificationUID;
	
	return admin.database().ref("/societyServiceNotifications").child("all").child(notificationUID).once('value').then(queryResult => {
		const status = queryResult.val().status;
		const userUID = queryResult.val().userUID;
		const eventTitle = queryResult.val().eventTitle;
		const eventDate = queryResult.val().eventDate;
		const timeSlot = queryResult.val().timeSlot;
		var notificationMesage;

		if(status === "Booking Accepted"){
			notificationMesage = "Your request for the Event, "+eventTitle+", on "+eventDate+", "+timeSlot+" has been accepted";
		} else{
			notificationMesage = "Your request for the Event, "+eventTitle+", on "+eventDate+", "+timeSlot+" has been rejected";
		}			
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
			
			const tokenId = queryResult.val().tokenId;
			
			console.log("Token id -> "+tokenId);
			
			const payload = {
				notification: {
                    title: "Namma Apartments",
                    body: notificationMesage,
                    "sound": "default",
                    "badge": "1"
				},
				data: {
					message: notificationMesage,
					type: "Event_Management"
				}
			};
			
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
			
		});
	});
	
});

// Notifications triggered when society service accepts User's Society Service request

exports.societyServiceResponseNotifications = functions.database.ref('/societyServiceNotifications/all/{notificationUID}/takenBy')
.onCreate((change, context) => {
	
	const notificationUID = context.params.notificationUID;
	
	return admin.database().ref("/societyServiceNotifications").child("all").child(notificationUID).once('value').then(queryResult => {
		const userUID = queryResult.val().userUID;
		const societyServiceType = queryResult.val().societyServiceType;
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
			
			const tokenId = queryResult.val().tokenId;
			
			console.log("Token id -> "+tokenId);
			
			const payload = {
				notification: {
                    title: "Namma Apartments",
                    body: "Your request for the "+societyServiceLookup[societyServiceType]+" Service has been accepted",
                    "sound": "default",
                    "badge": "1"
				},
				data: {
				message: "Your request for the "+societyServiceLookup[societyServiceType]+" Service has been accepted",
					type: "Society_Service"
				}
			};
			
			return admin.messaging().sendToDevice(tokenId, payload).then(result => {
				return console.log("Notification sent");
			});
			
		});
	});
	
});