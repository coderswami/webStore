'use strict';

angular.module('webstoreApp')
    .controller('CountryController', function ($scope, $state, Country, CountrySearch) {

        $scope.countrys = [];
        $scope.loadAll = function() {
            Country.query(function(result) {
               $scope.countrys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CountrySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.countrys = result;
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
            $scope.country = {
                code: null,
                name: null,
                isdCode: null,
                id: null
            };
        };
    });
