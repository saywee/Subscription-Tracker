/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

"use strict";

// create a new module, and load the other pluggable modules
var module = angular.module('SubTrek', ['ngResource', 'ngStorage']);

module.config(function ($sessionStorageProvider, $httpProvider) {
   // get the auth token from the session storage
   let authToken = $sessionStorageProvider.get('authToken');

   // does the auth token actually exist?
   if (authToken) {
      // add the token to all HTTP requests
      $httpProvider.defaults.headers.common.Authorization = 'Basic ' + authToken;
   }
});

module.factory('registerAPI', function ($resource) {
    return $resource('api/register');
});

module.factory('signInAPI', function ($resource) {
    return $resource('api/customers/:username');
});  
//module.factory('updateAccAPI', function ($resource){
//return $resource ('api/customers/:id');
//});
module.factory('updateAccAPI', function ($resource) {
        return $resource('api/customers/:username', null, {update: {method: 'PUT'}});
});

module.factory('deleteAccAPI', function ($resource) {
        return $resource('api/customers/:username');
});

module.controller('CustomerController', function (registerAPI, $window, signInAPI, $sessionStorage, updateAccAPI, $http, deleteAccAPI) { 
    this.signInMessage = "Please sign in to continue.";
    this.updateMessage = "";
    // alias 'this' so that we can access it inside callback functions
    let ctrl = this;
    
    this.registerCustomer = function (customer) {
        registerAPI.save(null, customer,
            // success callback
                function () {
                    $window.location = 'signin.html';
                },
                // error callback
                        function (error) {
                            console.log(error);
                        }
                 );
        console.log(customer);
    };   
    
    if ($sessionStorage.customer) {
        this.customer = $sessionStorage.customer;
     }
            
    this.signIn = function (username, password) {

        // generate authentication token
        let authToken = $window.btoa(username + ":" + password);

        // store token
        $sessionStorage.authToken = authToken;

        // add token to the HTTP request headers
        $http.defaults.headers.common.Authorization = 'Basic ' + authToken;

        // get customer from web service
        signInAPI.get({'username': username},
            // success callback
                function (customer) {
                    // also store the retrieved customer
                    $sessionStorage.customer = customer;

                    // redirect to home
                    $window.location = 'home.html';
                },
                
                // fail callback
                        function () {
                            ctrl.signInMessage = 'Sign in failed. Please try again.';
                        }
                );
        };
                    
        this.checkSignIn = function () {
            // has the customer been added to the session?
            if ($sessionStorage.customer) {
                this.signedIn = true;
                this.welcome = "Welcome " + $sessionStorage.customer.firstName + ". The current date is " + (new Date()).toLocaleDateString() + ".";
            } else {
                this.signedIn = false;
            }
        };  
                    
        this.signOut = function () {
            $sessionStorage.$reset();
            $window.location = 'home.html';
        }
        
        this.deleteCustomer = function (customer) {
        
            if ($window.confirm("Are you sure you want to delete your account?")) {
                console.log(customer.username);
                deleteAccAPI.delete({'username': customer.username}, function () {
                    $window.location = 'home.html';
                    // get subscriptions and categories again so we don't have to refresh
//                    ctrl.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
//                    ctrl.categories = categoryAPI.query({'username': $sessionStorage.customer.username});
                });
            }
        }
        
        this.updateCustomer = function (customer) {
            updateAccAPI.update({'username': customer.username}, customer, function () {
    //            ctrl.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
    //            $window.location = 'home.html';
                ctrl.updateMessage = "Account updated";
            });
        };
    });


module.factory('addSubscriptionAPI', function ($resource) {
    return $resource('api/subscriptions');
});

module.factory('updateSubAPI', function ($resource) {
    return $resource('api/subscriptions/:id', null, {update: {method: 'PUT'}});
});

module.factory('subscriptionAPI', function ($resource) {
    return $resource('api/subscriptions/:username'); 
});

module.factory('deleteAPI', function ($resource) {
    return $resource('api/subscriptions/:id');
});

module.factory('categoryAPI', function ($resource) {
    return $resource('api/categories/:username'); 
});

module.factory('sortAPI', function($resource){
    return $resource('api/sort/:username');
});

module.factory('filterAPI', function ($resource) {
    return $resource('api/categories/:category/:username');
});

module.factory('totalAPI', function ($resource) {
    return $resource('api/total/:username'); 
});

module.controller('SubscriptionController', function ($sessionStorage, addSubscriptionAPI,
        subscriptionAPI, deleteAPI, $window, categoryAPI, filterAPI, totalAPI, sortAPI, updateSubAPI) {
    let ctrl = this;

    this.total = totalAPI.get({'username': $sessionStorage.customer.username});


    console.log("Subscription controller initialized");

    if ($sessionStorage.customer) {
        this.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
    }

     if ($sessionStorage.updatingSub) {
        ctrl.subscription = $sessionStorage.updatingSub;
     }

    this.update = function(subby) {

        $sessionStorage.updatingSub = subby;
        $window.location = 'updatesub.html';
    };

    this.addSubscription = function (subscription) {
        subscription.customer = $sessionStorage.customer;

        addSubscriptionAPI.save(subscription,
                function () {
                    $window.location = 'home.html';
                },
                function (error) {
                    console.log(error);
                }
        );
        console.log(subscription + " for " + subscription.customer);
    };

    this.getSubscriptions = function (username) {
        this.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
    };

    this.deleteSubscription = function (subscription) {

        // ask the user before deleting
        if ($window.confirm("Are you sure you want to delete " + subscription.name + "?")) {
            deleteAPI.delete({'id': subscription.subscriptionId}, function () {
                // get subscriptions and categories again so we don't have to refresh
                ctrl.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
                ctrl.categories = categoryAPI.query({'username': $sessionStorage.customer.username});
                ctrl.total = totalAPI.get({'username': $sessionStorage.customer.username});
            });
        }

    };

    this.getSubscriptionStatus = function (subscription) {

        // calculate number of days remaining
        var numberOfDays = this.daysToToday(subscription.dueDate);

        // decide whether to add an 's' or not
        var plurality = numberOfDays > 1 ? 's' : '';

        // declare and initialize status variables
        var status = "error";
        var statusElement = document.getElementById("sub-" + subscription.subscriptionId);
        var renewElement = document.getElementById("renew-" + subscription.subscriptionId);

        // 3 days before reminder
        var reminderThreshold = 3;

        if (numberOfDays > 0) {
            status = "(in " + numberOfDays + " day" + plurality + ")";

            // check for null to avoid errors
            if (renewElement != null) {
                // hide the Renew button for non-expired subs
                renewElement.style.display = "none";
            }

            if (numberOfDays > reminderThreshold) {
                //statusElement.style.color = "green";
            } else {
                statusElement.style.color = "orange"; // dark orange
            }
        } else {
            status = "(expired)";
            statusElement.style.color = "red";
        }

        return status;
    };

    this.getConvertedDate = function (date) {   
        //   console.log((new Date(currentValue)).toLocaleDateString());
        return (new Date(date)).toLocaleDateString('en-NZ');
    };

    this.daysToToday = function (dateString) {

        var date = new Date(dateString);
        var today = new Date();

        // The number of milliseconds in one day
        const ONE_DAY = 1000 * 60 * 60 * 24;              

        // Calculate the difference in milliseconds
        const differenceMs = date - today;

        // Convert to days
        const numberOfDays = Math.round(differenceMs / ONE_DAY)

        return numberOfDays;

    };

    if ($sessionStorage.customer) {
        this.categories = categoryAPI.query({'username': $sessionStorage.customer.username});
    } else {
        $sessionStorage.$reset();
    }
    
    this.filterCat = function (selectedCat) {
        this.subscriptions = filterAPI.query({'category': selectedCat,'username' : $sessionStorage.customer.username});  
    };
    
    this.selectAll = function(){
        this.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
    };

    this.sort = function(){
        this.subscriptions = sortAPI.query({'username': $sessionStorage.customer.username});
    };

    this.updateSubscription = function (subscription) {
        updateSubAPI.update({'id': subscription.subscriptionId}, subscription, function () {
            ctrl.subscriptions = subscriptionAPI.query({'username': $sessionStorage.customer.username});
            $window.location = 'home.html';
        });
    };

    this.renewSubscription = function (subscription) {
        var newDueDate = new Date(subscription.dueDate);
        var today = new Date();

        newDueDate.setDate(newDueDate.getDate() - 1);

        // update the due date so that it is no longer expired
        while (newDueDate < today) {
            newDueDate.setMonth(newDueDate.getMonth() + 1);
        }

        var newDueDateString = newDueDate.toISOString();
        subscription.dueDate = newDueDateString;

        this.updateSubscription(subscription);

//                console.log(newDueDateString);
    };
});