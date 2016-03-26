'use strict';

angular.module('webstoreApp')
    .factory('Payment', function ($resource, DateUtils) {
        return $resource('api/payments/:id', {}, {
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
