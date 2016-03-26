'use strict';

angular.module('webstoreApp')
    .controller('ProductReviewDetailController', function ($scope, $rootScope, $stateParams, entity, ProductReview, Product, UserProfile) {
        $scope.productReview = entity;
        $scope.load = function (id) {
            ProductReview.get({id: id}, function(result) {
                $scope.productReview = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:productReviewUpdate', function(event, result) {
            $scope.productReview = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
