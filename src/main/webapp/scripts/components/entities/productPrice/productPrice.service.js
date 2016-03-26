'use strict';

angular.module('webstoreApp')
    .factory('ProductPrice', function ($resource, DateUtils) {
        return $resource('api/productPrices/:id', {}, {
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
