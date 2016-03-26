'use strict';

angular.module('webstoreApp')
    .controller('ProductAttrController', function ($scope, $state, ProductAttr, ProductAttrSearch) {

        $scope.productAttrs = [];
        $scope.loadAll = function() {
            ProductAttr.query(function(result) {
               $scope.productAttrs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductAttrSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productAttrs = result;
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
            $scope.productAttr = {
                name: null,
                description: null,
                value: null,
                id: null
            };
        };
    });
