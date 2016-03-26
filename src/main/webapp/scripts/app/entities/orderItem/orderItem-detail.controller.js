'use strict';

angular.module('webstoreApp')
    .controller('OrderItemDetailController', function ($scope, $rootScope, $stateParams, entity, OrderItem, OrderHeader, Product) {
        $scope.orderItem = entity;
        $scope.load = function (id) {
            OrderItem.get({id: id}, function(result) {
                $scope.orderItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:orderItemUpdate', function(event, result) {
            $scope.orderItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
