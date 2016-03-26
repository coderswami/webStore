'use strict';

angular.module('webstoreApp')
    .factory('Catalog', function ($resource, DateUtils) {
        return $resource('api/catalogs/:id', {}, {
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
