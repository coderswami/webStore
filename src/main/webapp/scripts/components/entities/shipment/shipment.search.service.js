'use strict';

angular.module('webstoreApp')
    .factory('ShipmentSearch', function ($resource) {
        return $resource('api/_search/shipments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
