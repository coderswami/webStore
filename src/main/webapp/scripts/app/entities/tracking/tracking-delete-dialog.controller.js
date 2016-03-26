'use strict';

angular.module('webstoreApp')
	.controller('TrackingDeleteController', function($scope, $uibModalInstance, entity, Tracking) {

        $scope.tracking = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Tracking.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
