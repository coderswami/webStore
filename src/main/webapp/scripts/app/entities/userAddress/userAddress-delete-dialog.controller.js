'use strict';

angular.module('webstoreApp')
	.controller('UserAddressDeleteController', function($scope, $uibModalInstance, entity, UserAddress) {

        $scope.userAddress = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserAddress.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
