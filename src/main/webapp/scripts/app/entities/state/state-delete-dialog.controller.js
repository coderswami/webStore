'use strict';

angular.module('webstoreApp')
	.controller('StateDeleteController', function($scope, $uibModalInstance, entity, State) {

        $scope.state = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            State.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
