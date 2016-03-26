'use strict';

angular.module('webstoreApp').controller('ProductAttrDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductAttr', 'Product',
        function($scope, $stateParams, $uibModalInstance, entity, ProductAttr, Product) {

        $scope.productAttr = entity;
        $scope.products = Product.query();
        $scope.load = function(id) {
            ProductAttr.get({id : id}, function(result) {
                $scope.productAttr = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:productAttrUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.productAttr.id != null) {
                ProductAttr.update($scope.productAttr, onSaveSuccess, onSaveError);
            } else {
                ProductAttr.save($scope.productAttr, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
