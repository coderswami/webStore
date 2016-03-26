'use strict';

angular.module('webstoreApp')
    .controller('StateController', function ($scope, $state, State, StateSearch) {

        $scope.states = [];
        $scope.loadAll = function() {
            State.query(function(result) {
               $scope.states = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            StateSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.states = result;
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
            $scope.state = {
                code: null,
                name: null,
                id: null
            };
        };
    });
