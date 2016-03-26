'use strict';

angular.module('webstoreApp')
    .factory('Shipment', function ($resource, DateUtils) {
        return $resource('api/shipments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
