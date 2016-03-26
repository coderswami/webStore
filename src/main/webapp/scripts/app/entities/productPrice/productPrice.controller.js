'use strict';

angular.module('webstoreApp')
    .controller('ProductPriceController', function ($scope, $state, ProductPrice, ProductPriceSearch) {

        $scope.productPrices = [];
        $scope.loadAll = function() {
            ProductPrice.query(function(result) {
               $scope.productPrices = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductPriceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productPrices = result;
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
            $scope.productPrice = {
                listPrice: null,
                discount: null,
                salesPrice: null,
                active: false,
                id: null
            };
        };
    });
