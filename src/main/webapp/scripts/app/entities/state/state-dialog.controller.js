'use strict';

angular.module('webstoreApp').controller('StateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'State', 'Country',
        function($scope, $stateParams, $uibModalInstance, entity, State, Country) {

        $scope.state = entity;
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            State.get({id : id}, function(result) {
                $scope.state = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:stateUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.state.id != null) {
                State.update($scope.state, onSaveSuccess, onSaveError);
            } else {
                State.save($scope.state, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
