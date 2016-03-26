'use strict';

angular.module('webstoreApp').controller('UserProfileDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserProfile', 'UserLogin', 'UserRole', 'OrderHeader', 'UserAddress',
        function($scope, $stateParams, $uibModalInstance, $q, entity, UserProfile, UserLogin, UserRole, OrderHeader, UserAddress) {

        $scope.userProfile = entity;
        $scope.authentications = UserLogin.query({filter: 'userprofile-is-null'});
        $q.all([$scope.userProfile.$promise, $scope.authentications.$promise]).then(function() {
            if (!$scope.userProfile.authentication || !$scope.userProfile.authentication.id) {
                return $q.reject();
            }
            return UserLogin.get({id : $scope.userProfile.authentication.id}).$promise;
        }).then(function(authentication) {
            $scope.authentications.push(authentication);
        });
        $scope.userroles = UserRole.query();
        $scope.orderheaders = OrderHeader.query();
        $scope.useraddresss = UserAddress.query();
        $scope.load = function(id) {
            UserProfile.get({id : id}, function(result) {
                $scope.userProfile = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:userProfileUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userProfile.id != null) {
                UserProfile.update($scope.userProfile, onSaveSuccess, onSaveError);
            } else {
                UserProfile.save($scope.userProfile, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDob = {};

        $scope.datePickerForDob.status = {
            opened: false
        };

        $scope.datePickerForDobOpen = function($event) {
            $scope.datePickerForDob.status.opened = true;
        };
}]);
