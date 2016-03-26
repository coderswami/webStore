'use strict';

angular.module('webstoreApp')
    .controller('TrackingController', function ($scope, $state, Tracking, TrackingSearch) {

        $scope.trackings = [];
        $scope.loadAll = function() {
            Tracking.query(function(result) {
               $scope.trackings = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            TrackingSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.trackings = result;
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
            $scope.tracking = {
                status: null,
                details: null,
                id: null
            };
        };
    });
