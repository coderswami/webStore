'use strict';

angular.module('webstoreApp').controller('UserAddressDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserAddress', 'UserProfile', 'Country', 'State',
        function($scope, $stateParams, $uibModalInstance, $q, entity, UserAddress, UserProfile, Country, State) {

        $scope.userAddress = entity;
        $scope.userprofiles = UserProfile.query();
        $scope.countrys = Country.query({filter: 'useraddress-is-null'});
        $q.all([$scope.userAddress.$promise, $scope.countrys.$promise]).then(function() {
            if (!$scope.userAddress.country || !$scope.userAddress.country.id) {
                return $q.reject();
            }
            return Country.get({id : $scope.userAddress.country.id}).$promise;
        }).then(function(country) {
            $scope.countrys.push(country);
        });
        $scope.states = State.query({filter: 'useraddress-is-null'});
        $q.all([$scope.userAddress.$promise, $scope.states.$promise]).then(function() {
            if (!$scope.userAddress.state || !$scope.userAddress.state.id) {
                return $q.reject();
            }
            return State.get({id : $scope.userAddress.state.id}).$promise;
        }).then(function(state) {
            $scope.states.push(state);
        });
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
