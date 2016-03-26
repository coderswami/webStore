'use strict';

angular.module('webstoreApp').controller('TrackingDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tracking', 'OrderHeader',
        function($scope, $stateParams, $uibModalInstance, entity, Tracking, OrderHeader) {

        $scope.tracking = entity;
        $scope.orderheaders = OrderHeader.query();
        $scope.load = function(id) {
            Tracking.get({id : id}, function(result) {
                $scope.tracking = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstoreApp:trackingUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tracking.id != null) {
                Tracking.update($scope.tracking, onSaveSuccess, onSaveError);
            } else {
                Tracking.save($scope.tracking, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
