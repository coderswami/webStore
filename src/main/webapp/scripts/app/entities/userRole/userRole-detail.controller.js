'use strict';

angular.module('webstoreApp')
    .controller('UserRoleDetailController', function ($scope, $rootScope, $stateParams, entity, UserRole, UserProfile) {
        $scope.userRole = entity;
        $scope.load = function (id) {
            UserRole.get({id: id}, function(result) {
                $scope.userRole = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:userRoleUpdate', function(event, result) {
            $scope.userRole = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
