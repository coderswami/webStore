'use strict';

angular.module('webstoreApp')
	.controller('OrderHeaderDeleteController', function($scope, $uibModalInstance, entity, OrderHeader) {

        $scope.orderHeader = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            OrderHeader.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
