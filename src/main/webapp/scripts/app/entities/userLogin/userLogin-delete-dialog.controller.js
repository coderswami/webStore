'use strict';

angular.module('webstoreApp')
	.controller('UserLoginDeleteController', function($scope, $uibModalInstance, entity, UserLogin) {

        $scope.userLogin = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserLogin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
