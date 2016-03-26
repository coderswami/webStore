'use strict';

angular.module('webstoreApp')
    .controller('TrackingDetailController', function ($scope, $rootScope, $stateParams, entity, Tracking, OrderHeader) {
        $scope.tracking = entity;
        $scope.load = function (id) {
            Tracking.get({id: id}, function(result) {
                $scope.tracking = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:trackingUpdate', function(event, result) {
            $scope.tracking = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
