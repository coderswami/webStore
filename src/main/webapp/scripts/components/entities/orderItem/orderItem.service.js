'use strict';

angular.module('webstoreApp')
    .factory('OrderItem', function ($resource, DateUtils) {
        return $resource('api/orderItems/:id', {}, {
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
