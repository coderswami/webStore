'use strict';

angular.module('webstoreApp')
    .factory('UserRole', function ($resource, DateUtils) {
        return $resource('api/userRoles/:id', {}, {
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
