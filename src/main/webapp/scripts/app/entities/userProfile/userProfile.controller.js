'use strict';

angular.module('webstoreApp')
    .controller('UserProfileController', function ($scope, $state, UserProfile, UserProfileSearch) {

        $scope.userProfiles = [];
        $scope.loadAll = function() {
            UserProfile.query(function(result) {
               $scope.userProfiles = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            UserProfileSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userProfiles = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userProfile = {
                email: null,
                fname: null,
                lname: null,
                imageUrl: null,
                gender: null,
                dob: null,
                id: null
            };
        };
    });
