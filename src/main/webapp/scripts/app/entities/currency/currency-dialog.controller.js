'use strict';

angular.module('webstoreApp').controller('CurrencyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Currency',
        function($scope, $stateParams, $uibModalInstance, entity, Currency) {

        $scope.currency = entity;
        $scope.load = function(id) {
            Currency.get({id : id}, function(result) {
                $scope.currency = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:currencyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.currency.id != null) {
                Currency.update($scope.currency, onSaveSuccess, onSaveError);
            } else {
                Currency.save($scope.currency, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
