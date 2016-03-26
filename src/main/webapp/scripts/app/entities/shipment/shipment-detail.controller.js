'use strict';

angular.module('webstoreApp')
    .controller('ShipmentDetailController', function ($scope, $rootScope, $stateParams, entity, Shipment, UserAddress) {
        $scope.shipment = entity;
        $scope.load = function (id) {
            Shipment.get({id: id}, function(result) {
                $scope.shipment = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:shipmentUpdate', function(event, result) {
            $scope.shipment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
