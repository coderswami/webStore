'use strict';

angular.module('webstoreApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, Category, ProductAttr, ProductPrice, ProductReview) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
