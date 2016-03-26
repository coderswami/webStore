'use strict';

angular.module('webstoreApp')
    .factory('OrderHeaderSearch', function ($resource) {
        return $resource('api/_search/orderHeaders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
