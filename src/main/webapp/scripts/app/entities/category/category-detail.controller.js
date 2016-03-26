'use strict';

angular.module('webstoreApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, Catalog, Product) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
