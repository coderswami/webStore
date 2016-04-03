'use strict';

angular.module('webstoreApp').controller('UserAddressDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserAddress', 'UserProfile', 'Country', 'State',
        function($scope, $stateParams, $uibModalInstance, entity, UserAddress, UserProfile, Country, State) {

        $scope.userAddress = entity;
        $scope.userprofiles = UserProfile.query();
        $scope.countrys = Country.query();
        $scope.states = State.query();
        $scope.load = function(id) {
            UserAddress.get({id : id}, function(result) {
                $scope.userAddress = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:userAddressUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userAddress.id != null) {
                UserAddress.update($scope.userAddress, onSaveSuccess, onSaveError);
            } else {
                UserAddress.save($scope.userAddress, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
