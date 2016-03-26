'use strict';

angular.module('webstoreApp')
    .controller('OrderHeaderDetailController', function ($scope, $rootScope, $stateParams, entity, OrderHeader, UserProfile, OrderItem, Payment, Shipment, Tracking) {
        $scope.orderHeader = entity;
        $scope.load = function (id) {
            OrderHeader.get({id: id}, function(result) {
                $scope.orderHeader = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:orderHeaderUpdate', function(event, result) {
            $scope.orderHeader = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
