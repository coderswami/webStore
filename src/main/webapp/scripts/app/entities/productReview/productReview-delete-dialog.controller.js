'use strict';

angular.module('webstoreApp')
	.controller('ProductReviewDeleteController', function($scope, $uibModalInstance, entity, ProductReview) {

        $scope.productReview = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProductReview.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
