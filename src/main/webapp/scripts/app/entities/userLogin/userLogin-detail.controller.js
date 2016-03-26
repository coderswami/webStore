'use strict';

angular.module('webstoreApp')
    .controller('UserLoginDetailController', function ($scope, $rootScope, $stateParams, entity, UserLogin) {
        $scope.userLogin = entity;
        $scope.load = function (id) {
            UserLogin.get({id: id}, function(result) {
                $scope.userLogin = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:userLoginUpdate', function(event, result) {
            $scope.userLogin = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
