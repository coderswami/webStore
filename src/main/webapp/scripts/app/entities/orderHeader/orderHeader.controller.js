'use strict';

angular.module('webstoreApp')
    .controller('OrderHeaderController', function ($scope, $state, OrderHeader, OrderHeaderSearch) {

        $scope.orderHeaders = [];
        $scope.loadAll = function() {
            OrderHeader.query(function(result) {
               $scope.orderHeaders = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            OrderHeaderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.orderHeaders = result;
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
            $scope.orderHeader = {
                type: null,
                status: null,
                orderTotal: null,
                id: null
            };
        };
    });
