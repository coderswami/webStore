'use strict';

angular.module('webstoreApp').controller('OrderItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OrderItem', 'OrderHeader', 'Product',
        function($scope, $stateParams, $uibModalInstance, $q, entity, OrderItem, OrderHeader, Product) {

        $scope.orderItem = entity;
        $scope.orderheaders = OrderHeader.query();
        $scope.products = Product.query({filter: 'orderitem-is-null'});
        $q.all([$scope.orderItem.$promise, $scope.products.$promise]).then(function() {
            if (!$scope.orderItem.product || !$scope.orderItem.product.id) {
                return $q.reject();
            }
            return Product.get({id : $scope.orderItem.product.id}).$promise;
        }).then(function(product) {
            $scope.products.push(product);
        });
        $scope.load = function(id) {
            OrderItem.get({id : id}, function(result) {
                $scope.orderItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:orderItemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.orderItem.id != null) {
                OrderItem.update($scope.orderItem, onSaveSuccess, onSaveError);
            } else {
                OrderItem.save($scope.orderItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
