'use strict';

angular.module('webstoreApp')
    .controller('CountryDetailController', function ($scope, $rootScope, $stateParams, entity, Country, State, Catalog) {
        $scope.country = entity;
        $scope.load = function (id) {
            Country.get({id: id}, function(result) {
                $scope.country = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:countryUpdate', function(event, result) {
            $scope.country = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
