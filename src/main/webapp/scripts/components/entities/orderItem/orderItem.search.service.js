'use strict';

angular.module('webstoreApp')
    .factory('OrderItemSearch', function ($resource) {
        return $resource('api/_search/orderItems/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
