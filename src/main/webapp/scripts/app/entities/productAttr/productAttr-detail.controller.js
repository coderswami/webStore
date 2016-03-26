'use strict';

angular.module('webstoreApp')
    .controller('ProductAttrDetailController', function ($scope, $rootScope, $stateParams, entity, ProductAttr, Product) {
        $scope.productAttr = entity;
        $scope.load = function (id) {
            ProductAttr.get({id: id}, function(result) {
                $scope.productAttr = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:productAttrUpdate', function(event, result) {
            $scope.productAttr = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
