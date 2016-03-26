'use strict';

angular.module('webstoreApp')
	.controller('ProductPriceDeleteController', function($scope, $uibModalInstance, entity, ProductPrice) {

        $scope.productPrice = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProductPrice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
