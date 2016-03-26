'use strict';

angular.module('webstoreApp')
    .factory('OrderHeader', function ($resource, DateUtils) {
        return $resource('api/orderHeaders/:id', {}, {
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
