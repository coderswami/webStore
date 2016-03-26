'use strict';

angular.module('webstoreApp')
    .controller('StateDetailController', function ($scope, $rootScope, $stateParams, entity, State, Country) {
        $scope.state = entity;
        $scope.load = function (id) {
            State.get({id: id}, function(result) {
                $scope.state = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:stateUpdate', function(event, result) {
            $scope.state = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
