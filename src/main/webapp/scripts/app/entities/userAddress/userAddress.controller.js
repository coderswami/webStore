'use strict';

angular.module('webstoreApp')
    .controller('UserAddressController', function ($scope, $state, UserAddress, UserAddressSearch) {

        $scope.userAddresss = [];
        $scope.loadAll = function() {
            UserAddress.query(function(result) {
               $scope.userAddresss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            UserAddressSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userAddresss = result;
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
            $scope.userAddress = {
                name: null,
                streetAddress: null,
                landmark: null,
                city: null,
                pin: null,
                phone: null,
                id: null
            };
        };
    });
