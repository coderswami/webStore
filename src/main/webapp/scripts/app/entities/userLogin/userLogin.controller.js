'use strict';

angular.module('webstoreApp')
    .controller('UserLoginController', function ($scope, $state, UserLogin, UserLoginSearch) {

        $scope.userLogins = [];
        $scope.loadAll = function() {
            UserLogin.query(function(result) {
               $scope.userLogins = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            UserLoginSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userLogins = result;
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
            $scope.userLogin = {
                username: null,
                password: null,
                id: null
            };
        };
    });
