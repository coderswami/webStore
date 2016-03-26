'use strict';

angular.module('webstoreApp').controller('UserRoleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserRole', 'UserProfile',
        function($scope, $stateParams, $uibModalInstance, entity, UserRole, UserProfile) {

        $scope.userRole = entity;
        $scope.userprofiles = UserProfile.query();
        $scope.load = function(id) {
            UserRole.get({id : id}, function(result) {
                $scope.userRole = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:userRoleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userRole.id != null) {
                UserRole.update($scope.userRole, onSaveSuccess, onSaveError);
            } else {
                UserRole.save($scope.userRole, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
