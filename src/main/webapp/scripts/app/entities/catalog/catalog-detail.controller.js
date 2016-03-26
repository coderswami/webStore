'use strict';

angular.module('webstoreApp')
    .controller('CatalogDetailController', function ($scope, $rootScope, $stateParams, entity, Catalog, Country, Category) {
        $scope.catalog = entity;
        $scope.load = function (id) {
            Catalog.get({id: id}, function(result) {
                $scope.catalog = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:catalogUpdate', function(event, result) {
            $scope.catalog = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
