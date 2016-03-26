'use strict';

angular.module('webstoreApp')
	.controller('CountryDeleteController', function($scope, $uibModalInstance, entity, Country) {

        $scope.country = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Country.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
