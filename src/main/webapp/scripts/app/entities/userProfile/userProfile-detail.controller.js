'use strict';

angular.module('webstoreApp')
    .controller('UserProfileDetailController', function ($scope, $rootScope, $stateParams, entity, UserProfile, UserLogin, UserRole, OrderHeader, UserAddress) {
        $scope.userProfile = entity;
        $scope.load = function (id) {
            UserProfile.get({id: id}, function(result) {
                $scope.userProfile = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:userProfileUpdate', function(event, result) {
            $scope.userProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
