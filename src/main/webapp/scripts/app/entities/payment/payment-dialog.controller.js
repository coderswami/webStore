'use strict';

angular.module('webstoreApp').controller('PaymentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payment',
        function($scope, $stateParams, $uibModalInstance, entity, Payment) {

        $scope.payment = entity;
        $scope.load = function(id) {
            Payment.get({id : id}, function(result) {
                $scope.payment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:paymentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.payment.id != null) {
                Payment.update($scope.payment, onSaveSuccess, onSaveError);
            } else {
                Payment.save($scope.payment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
