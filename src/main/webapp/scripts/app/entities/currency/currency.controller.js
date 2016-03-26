'use strict';

angular.module('webstoreApp')
    .controller('CurrencyController', function ($scope, $state, Currency, CurrencySearch) {

        $scope.currencys = [];
        $scope.loadAll = function() {
            Currency.query(function(result) {
               $scope.currencys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CurrencySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.currencys = result;
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
            $scope.currency = {
                code: null,
                description: null,
                id: null
            };
        };
    });
