'use strict';

angular.module('webstoreApp').controller('ProductPriceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProductPrice', 'Product', 'Currency',
        function($scope, $stateParams, $uibModalInstance, $q, entity, ProductPrice, Product, Currency) {

        $scope.productPrice = entity;
        $scope.products = Product.query();
        $scope.currencys = Currency.query({filter: 'productprice-is-null'});
        $q.all([$scope.productPrice.$promise, $scope.currencys.$promise]).then(function() {
            if (!$scope.productPrice.currency || !$scope.productPrice.currency.id) {
                return $q.reject();
            }
            return Currency.get({id : $scope.productPrice.currency.id}).$promise;
        }).then(function(currency) {
            $scope.currencys.push(currency);
        });
        $scope.load = function(id) {
            ProductPrice.get({id : id}, function(result) {
                $scope.productPrice = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:productPriceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.productPrice.id != null) {
                ProductPrice.update($scope.productPrice, onSaveSuccess, onSaveError);
            } else {
                ProductPrice.save($scope.productPrice, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
