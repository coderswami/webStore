'use strict';

angular.module('webstoreApp').controller('CountryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Country', 'State', 'Catalog',
        function($scope, $stateParams, $uibModalInstance, entity, Country, State, Catalog) {

        $scope.country = entity;
        $scope.states = State.query();
        $scope.catalogs = Catalog.query();
        $scope.load = function(id) {
            Country.get({id : id}, function(result) {
                $scope.country = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:countryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.country.id != null) {
                Country.update($scope.country, onSaveSuccess, onSaveError);
            } else {
                Country.save($scope.country, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
