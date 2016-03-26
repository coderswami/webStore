'use strict';

angular.module('webstoreApp')
    .controller('OrderItemController', function ($scope, $state, OrderItem, OrderItemSearch) {

        $scope.orderItems = [];
        $scope.loadAll = function() {
            OrderItem.query(function(result) {
               $scope.orderItems = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            OrderItemSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.orderItems = result;
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
            $scope.orderItem = {
                quantity: null,
                price: null,
                status: null,
                id: null
            };
        };
    });
