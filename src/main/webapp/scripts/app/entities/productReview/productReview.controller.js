'use strict';

angular.module('webstoreApp')
    .controller('ProductReviewController', function ($scope, $state, ProductReview, ProductReviewSearch) {

        $scope.productReviews = [];
        $scope.loadAll = function() {
            ProductReview.query(function(result) {
               $scope.productReviews = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductReviewSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productReviews = result;
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
            $scope.productReview = {
                title: null,
                description: null,
                rating: null,
                id: null
            };
        };
    });
