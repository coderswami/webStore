'use strict';

angular.module('webstoreApp').controller('CatalogDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Catalog', 'Country', 'Category',
        function($scope, $stateParams, $uibModalInstance, entity, Catalog, Country, Category) {

        $scope.catalog = entity;
        $scope.countrys = Country.query();
        $scope.categorys = Category.query();
        $scope.load = function(id) {
            Catalog.get({id : id}, function(result) {
                $scope.catalog = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:catalogUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.catalog.id != null) {
                Catalog.update($scope.catalog, onSaveSuccess, onSaveError);
            } else {
                Catalog.save($scope.catalog, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
