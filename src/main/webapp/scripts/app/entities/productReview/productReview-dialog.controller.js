'use strict';

angular.module('webstoreApp').controller('ProductReviewDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProductReview', 'Product', 'UserProfile',
        function($scope, $stateParams, $uibModalInstance, $q, entity, ProductReview, Product, UserProfile) {

        $scope.productReview = entity;
        $scope.products = Product.query();
        $scope.users = UserProfile.query({filter: 'productreview-is-null'});
        $q.all([$scope.productReview.$promise, $scope.users.$promise]).then(function() {
            if (!$scope.productReview.user || !$scope.productReview.user.id) {
                return $q.reject();
            }
            return UserProfile.get({id : $scope.productReview.user.id}).$promise;
        }).then(function(user) {
            $scope.users.push(user);
        });
        $scope.load = function(id) {
            ProductReview.get({id : id}, function(result) {
                $scope.productReview = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:productReviewUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.productReview.id != null) {
                ProductReview.update($scope.productReview, onSaveSuccess, onSaveError);
            } else {
                ProductReview.save($scope.productReview, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
