'use strict';

angular.module('webstoreApp')
    .controller('UserRoleController', function ($scope, $state, UserRole, UserRoleSearch) {

        $scope.userRoles = [];
        $scope.loadAll = function() {
            UserRole.query(function(result) {
               $scope.userRoles = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            UserRoleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userRoles = result;
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
            $scope.userRole = {
                role: null,
                id: null
            };
        };
    });
