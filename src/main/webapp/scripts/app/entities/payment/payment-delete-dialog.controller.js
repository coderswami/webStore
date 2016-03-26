'use strict';

angular.module('webstoreApp')
	.controller('PaymentDeleteController', function($scope, $uibModalInstance, entity, Payment) {

        $scope.payment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Payment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
