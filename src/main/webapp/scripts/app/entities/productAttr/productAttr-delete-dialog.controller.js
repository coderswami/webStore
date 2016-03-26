'use strict';

angular.module('webstoreApp')
	.controller('ProductAttrDeleteController', function($scope, $uibModalInstance, entity, ProductAttr) {

        $scope.productAttr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProductAttr.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
