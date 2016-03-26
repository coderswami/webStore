'use strict';

angular.module('webstoreApp')
    .controller('ProductPriceDetailController', function ($scope, $rootScope, $stateParams, entity, ProductPrice, Product, Currency) {
        $scope.productPrice = entity;
        $scope.load = function (id) {
            ProductPrice.get({id: id}, function(result) {
                $scope.productPrice = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:productPriceUpdate', function(event, result) {
            $scope.productPrice = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
