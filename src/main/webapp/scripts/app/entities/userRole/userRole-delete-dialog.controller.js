'use strict';

angular.module('webstoreApp')
	.controller('UserRoleDeleteController', function($scope, $uibModalInstance, entity, UserRole) {

        $scope.userRole = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserRole.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
