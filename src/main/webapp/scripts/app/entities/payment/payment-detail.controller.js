'use strict';

angular.module('webstoreApp')
    .controller('PaymentDetailController', function ($scope, $rootScope, $stateParams, entity, Payment) {
        $scope.payment = entity;
        $scope.load = function (id) {
            Payment.get({id: id}, function(result) {
                $scope.payment = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstoreApp:paymentUpdate', function(event, result) {
            $scope.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
