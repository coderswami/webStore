'use strict';

angular.module('webstoreApp')
	.controller('CurrencyDeleteController', function($scope, $uibModalInstance, entity, Currency) {

        $scope.currency = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Currency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
