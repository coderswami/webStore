'use strict';

angular.module('webstoreApp')
    .controller('PaymentController', function ($scope, $state, Payment, PaymentSearch) {

        $scope.payments = [];
        $scope.loadAll = function() {
            Payment.query(function(result) {
               $scope.payments = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PaymentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.payments = result;
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
            $scope.payment = {
                type: null,
                amount: null,
                status: null,
                id: null
            };
        };
    });
