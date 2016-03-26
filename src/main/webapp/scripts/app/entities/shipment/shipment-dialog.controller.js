'use strict';

angular.module('webstoreApp').controller('ShipmentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Shipment', 'UserAddress',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Shipment, UserAddress) {

        $scope.shipment = entity;
        $scope.addresss = UserAddress.query({filter: 'shipment-is-null'});
        $q.all([$scope.shipment.$promise, $scope.addresss.$promise]).then(function() {
            if (!$scope.shipment.address || !$scope.shipment.address.id) {
                return $q.reject();
            }
            return UserAddress.get({id : $scope.shipment.address.id}).$promise;
        }).then(function(address) {
            $scope.addresss.push(address);
        });
        $scope.load = function(id) {
            Shipment.get({id : id}, function(result) {
                $scope.shipment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:shipmentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.shipment.id != null) {
                Shipment.update($scope.shipment, onSaveSuccess, onSaveError);
            } else {
                Shipment.save($scope.shipment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
