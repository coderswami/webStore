'use strict';

angular.module('webstoreApp')
    .controller('CatalogController', function ($scope, $state, Catalog, CatalogSearch) {

        $scope.catalogs = [];
        $scope.loadAll = function() {
            Catalog.query(function(result) {
               $scope.catalogs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CatalogSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.catalogs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.catalog = {
                name: null,
                description: null,
                active: false,
                id: null
            };
        };
    });
