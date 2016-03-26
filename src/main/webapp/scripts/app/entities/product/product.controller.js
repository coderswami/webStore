'use strict';

angular.module('webstoreApp')
    .controller('ProductController', function ($scope, $state, Product, ProductSearch) {

        $scope.products = [];
        $scope.loadAll = function() {
            Product.query(function(result) {
               $scope.products = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.products = result;
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
            $scope.product = {
                name: null,
                description: null,
                imageUrl: null,
                inventory: null,
                status: null,
                active: false,
                id: null
            };
        };
    });
