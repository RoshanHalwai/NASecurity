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
     .then (userUIDList => {
        var userFlatDetails = [];
        userUIDList.forEach(function (userUID) {
            userFlatDetails.push(admin.database().ref('/users/private').child(userUID).child('flatDetails').once('value'));
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
                    return userFlatReference.child('pendingDues').child(month+year).set(maintenanceCost);
                });
        });
        console.log("Pending Dues Update Completed..")
        return res.redirect(200);
     })
     .catch(error => {console.log("Update Error")});
});


// Notifications related to Namma Apartments App (where user's action is required)

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
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult=>{
			
			const tokenId = queryResult.val().tokenId;
		
			const deviceType = queryResult.child("otherDetails").val().deviceType;
			
			if (deviceType === "android") {
				console.log("If condition entered");
				const payload = {
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
			} else {
				const payload = {
				notification: {
                    title: "Namma Apartments",
                    body: message,
                    "sound": "default",
                    "badge": "1",
                    "click_action": "actionCategory",
					"mutable_content": "true",
					"content-available":"1"
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
			}
		});
	});
});

// Nofications related to Namma Apartments App (where user's action is  not required)

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
				const userCity = queryResult.child("flatDetails").val().city;
				const userSocietyName = queryResult.child("flatDetails").val().societyName;
				const userApartmentName = queryResult.child("flatDetails").val().apartmentName;
				const userFlatNumber = queryResult.child("flatDetails").val().flatNumber;
				
				var payload;
				
				return admin.database().ref("/userData").child("private")
							.child(userCity).child(userSocietyName).child(userApartmentName).child(userFlatNumber).child("visitors").child(inviterUID).child(visitorUID)
							.once('value').then(queryResult => {
								
					const isVisitorAvailable = queryResult.val();
		
					if (!isVisitorAvailable) {
						return null;
					}
				
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
						
						const userSocietyName = queryResult.child("flatDetails").val().societyName;
						const userCity = queryResult.child("flatDetails").val().city;
						const userApartmentName = queryResult.child("flatDetails").val().apartmentName;
						const userFlatNumber = queryResult.child("flatDetails").val().flatNumber;
						
						return admin.database().ref("/userData").child("private")
							.child(userCity).child(userSocietyName).child(userApartmentName).child(userFlatNumber).child("dailyServices").child(dailyServiceType).child(dailyServiceUID)
							.once('value').then(queryResult => {
								
							const isDailyServiceWorking = queryResult.val();
				
							if (!isDailyServiceWorking) {
								return null;
							}
							
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
		const promises = [];
		
		return admin.database().ref("/noticeBoard").child(noticeBoardUID).once('value').then(queryResult => {
			const dateAndTime = queryResult.val().dateAndTime;
			const title = queryResult.val().title;	
			
			return admin.database().ref("users").child("private").once('value').then(queryResult => {
		
			return queryResult.forEach((userSnap) => {
				var userUID = userSnap.key;
				
				const userDataReference = admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
						const tokenId = queryResult.val().tokenId;
						const verified = queryResult.child("privileges").val().verified;
						
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

// Notifications triggered when society service accepts User's Society Service request

exports.societyServiceResponseNotifications = functions.database.ref('/societyServiceNotifications/all/{notificationUID}/takenBy')
.onCreate((change, context) => {
	
	const notificationUID = context.params.notificationUID;
	
	return admin.database().ref("/societyServiceNotifications").child("all").child(notificationUID).once('value').then(queryResult => {
		const userUID = queryResult.val().userUID;
		const societyServiceType = queryResult.val().societyServiceType;
		
		return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
			
			const tokenId = queryResult.val().tokenId;
			
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

// Nofications related to Namma Apartments Society Service App

// Notifications triggered when User sends notification to Society Service

exports.societyServiceNotifications = functions.database.ref('/userData/private/{city}/{society}/{apartment}/{flat}/societyServiceNotifications/{societyServiceType}/{notificationUID}')
.onCreate((change, context) => {

	const notificationUID = context.params.notificationUID;

	return admin.database().ref("/societyServiceNotifications").child("all").child(notificationUID).once('value').then(queryResult => {
		const societyServiceType = queryResult.val().societyServiceType;
		const ownerUID = queryResult.val().userUID;
		var eventDate = queryResult.val().eventDate;

		return admin.database().ref('/users').child("private").child(ownerUID).child("personalDetails").once('value').then(queryResult => {
			
			const userFullName = queryResult.val().fullName;
			
			return admin.database().ref('/users').child("private").child(ownerUID).child("flatDetails").once('value').then(queryResult => {
				
				const apartmentName = queryResult.val().apartmentName;
				const flatNumber = queryResult.val().flatNumber;
				var mobileNumber;
				var tokenId;
				
				if (societyServiceType === "eventManagement" || societyServiceType === "scrapCollection"){
					
					var notificationMessage;
					
					return admin.database().ref('/societyServices').child("admin").once('value').then(queryResult =>{		
				
						tokenId = queryResult.val().tokenId;
						
						if (societyServiceType === "eventManagement"){
							notificationMessage = userFullName +", "+ apartmentName +", "+ flatNumber +", "+" has booked the hall, for an event on "+ eventDate +". ";
						} else{
							notificationMessage = userFullName +", "+ apartmentName +", "+ flatNumber +", "+"wants to discard some Scrap items from the residence";
							eventDate = "";
						}
						
						const payload = {		
								data: {
									message: notificationMessage,
									notificationUID: notificationUID,
									societyServiceType : societyServiceType,
									eventDate : eventDate
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

// Notifications triggered when user cancels society service request

exports.userCancelsSocietyServiceRequestNotifications = functions.database.ref('/societyServiceNotifications/all/{notificationUID}/status')
.onUpdate((change, context) => {
	
		const notificationUID = context.params.notificationUID;
	
		return admin.database().ref("/societyServiceNotifications").child("all").child(notificationUID).once('value').then(queryResult => {
			
			const status = queryResult.val().status;
			const societyServiceType = queryResult.val().societyServiceType;
			const societyServiceUID = queryResult.val().takenBy;
			
			if (status === "Cancelled"){
				
					return admin.database().ref("/societyServices").child(societyServiceType).child("private").child("data").child(societyServiceUID).once('value').then(queryResult => {
					
						const tokenId = queryResult.val().tokenId;
					
						const payload = {
										data: {
											message: "Service has been Cancelled by a User",
											societyServiceType: "cancelledServiceRequest"
											}
										};
					
						return admin.messaging().sendToDevice(tokenId, payload).then(result => {
						
							return console.log("Notification sent");
						
						});
					});
			} else {
				
				return null;
				
			}
		});
});

// Notifications triggered to Society Admin when User request to donate food 

exports.donateFoodNotifications = functions.database.ref('/foodDonations/{foodDonationNotificationUID}')
.onCreate((change, context) => {
	
		const foodDonationNotificationUID = context.params.foodDonationNotificationUID;
		
		return admin.database().ref("/foodDonations").child(foodDonationNotificationUID).once('value').then(queryResult => {
			
			const userUID = queryResult.val().userUID;
			
			return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
				
				const userFullName = queryResult.child("personalDetails").val().fullName;
				const userApartmentName = queryResult.child("flatDetails").val().apartmentName;
				const userFlatNumber = queryResult.child("flatDetails").val().flatNumber;
				
				return admin.database().ref("/societyServices").child("admin").once('value').then(queryResult => {
					
					const tokenId = queryResult.val().tokenId;
					
					const payload = {
						data:{
							message : userFullName +", "+ userFlatNumber +", "+ userApartmentName +", wants to donate Food to the needy one's",
							societyServiceType: "userDonateFoodNotification"
						}
					};
					
					return admin.messaging().sendToDevice(tokenId, payload).then(result => {
						
						return console.log("Notification sent");
						
					});
				});
			});
			
		});	
});

// Notifications triggered to Society Admin when User raises 'Support' request 

exports.supportNotifications = functions.database.ref('/support/{supportUID}')
.onCreate((change, context) => {
	
		const supportUID = context.params.supportUID;
		
		return admin.database().ref("/support").child(supportUID).once('value').then(queryResult => {
			
			const userUID = queryResult.val().userUID;
			
			return admin.database().ref("/users").child("private").child(userUID).once('value').then(queryResult => {
				
				const userFullName = queryResult.child("personalDetails").val().fullName;
				const userApartmentName = queryResult.child("flatDetails").val().apartmentName;
				const userFlatNumber = queryResult.child("flatDetails").val().flatNumber;
				
				return admin.database().ref("/societyServices").child("admin").once('value').then(queryResult => {
					
					const tokenId = queryResult.val().tokenId;
					
					const payload = {
						data:{
							message : userFullName +", "+ userFlatNumber +", "+ userApartmentName +", has a raised a Support request"+". ",
							societyServiceType: "supportNotification"
						}
					};
					
					return admin.messaging().sendToDevice(tokenId, payload).then(result => {
						
						return console.log("Notification sent");
						
					});
				});
			});
			
		});	
});

// Nofications related to Namma Apartments Security App

// Notifications triggered when User Raises an Emergency Alarm. The Security Guard Admin and the Society Admin gets notified.

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
                        const guardTokenId = snapshot.val();
						
						return admin.database().ref("/societyServices").child("admin").once('value').then(queryResult => {
							const adminTokenId = queryResult.val().tokenId;
							console.log("Admin token is:", adminTokenId);

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
	
});