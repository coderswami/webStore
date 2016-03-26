'use strict';

angular.module('webstoreApp').controller('OrderHeaderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OrderHeader', 'UserProfile', 'OrderItem', 'Payment', 'Shipment', 'Tracking',
        function($scope, $stateParams, $uibModalInstance, $q, entity, OrderHeader, UserProfile, OrderItem, Payment, Shipment, Tracking) {

        $scope.orderHeader = entity;
        $scope.userprofiles = UserProfile.query();
        $scope.orderitems = OrderItem.query();
        $scope.payments = Payment.query({filter: 'orderheader-is-null'});
        $q.all([$scope.orderHeader.$promise, $scope.payments.$promise]).then(function() {
            if (!$scope.orderHeader.payment || !$scope.orderHeader.payment.id) {
                return $q.reject();
            }
            return Payment.get({id : $scope.orderHeader.payment.id}).$promise;
        }).then(function(payment) {
            $scope.payments.push(payment);
        });
        $scope.shipments = Shipment.query({filter: 'orderheader-is-null'});
        $q.all([$scope.orderHeader.$promise, $scope.shipments.$promise]).then(function() {
            if (!$scope.orderHeader.shipment || !$scope.orderHeader.shipment.id) {
                return $q.reject();
            }
            return Shipment.get({id : $scope.orderHeader.shipment.id}).$promise;
        }).then(function(shipment) {
            $scope.shipments.push(shipment);
        });
        $scope.trackings = Tracking.query();
        $scope.load = function(id) {
            OrderHeader.get({id : id}, function(result) {
                $scope.orderHeader = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:orderHeaderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.orderHeader.id != null) {
                OrderHeader.update($scope.orderHeader, onSaveSuccess, onSaveError);
            } else {
                OrderHeader.save($scope.orderHeader, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
