'use strict';

angular.module('webstoreApp')
    .controller('UserAddressDetailController', function ($scope, $rootScope, $stateParams, entity, UserAddress, UserProfile, Country, State) {
        $scope.userAddress = entity;
        $scope.load = function (id) {
            UserAddress.get({id: id}, function(result) {
                $scope.userAddress = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:userAddressUpdate', function(event, result) {
            $scope.userAddress = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
