'use strict';

angular.module('webstoreApp')
    .controller('ShipmentController', function ($scope, $state, Shipment, ShipmentSearch) {

        $scope.shipments = [];
        $scope.loadAll = function() {
            Shipment.query(function(result) {
               $scope.shipments = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ShipmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.shipments = result;
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
            $scope.shipment = {
                type: null,
                status: null,
                id: null
            };
        };
    });
