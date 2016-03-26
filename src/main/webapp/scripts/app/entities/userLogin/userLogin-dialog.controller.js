'use strict';

angular.module('webstoreApp').controller('UserLoginDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserLogin',
        function($scope, $stateParams, $uibModalInstance, entity, UserLogin) {

        $scope.userLogin = entity;
        $scope.load = function(id) {
            UserLogin.get({id : id}, function(result) {
                $scope.userLogin = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:userLoginUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userLogin.id != null) {
                UserLogin.update($scope.userLogin, onSaveSuccess, onSaveError);
            } else {
                UserLogin.save($scope.userLogin, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
