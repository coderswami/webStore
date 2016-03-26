'use strict';

angular.module('webstoreApp')
	.controller('ShipmentDeleteController', function($scope, $uibModalInstance, entity, Shipment) {

        $scope.shipment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Shipment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
