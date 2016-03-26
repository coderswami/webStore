'use strict';

angular.module('webstoreApp').controller('ProductDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Product', 'Category', 'ProductAttr', 'ProductPrice', 'ProductReview',
        function($scope, $stateParams, $uibModalInstance, entity, Product, Category, ProductAttr, ProductPrice, ProductReview) {

        $scope.product = entity;
        $scope.categorys = Category.query();
        $scope.productattrs = ProductAttr.query();
        $scope.productprices = ProductPrice.query();
        $scope.productreviews = ProductReview.query();
        $scope.load = function(id) {
            Product.get({id : id}, function(result) {
                $scope.product = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:productUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.product.id != null) {
                Product.update($scope.product, onSaveSuccess, onSaveError);
            } else {
                Product.save($scope.product, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
