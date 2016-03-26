'use strict';

angular.module('webstoreApp')
	.controller('OrderItemDeleteController', function($scope, $uibModalInstance, entity, OrderItem) {

        $scope.orderItem = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            OrderItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
