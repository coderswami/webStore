'use strict';

angular.module('webstoreApp')
    .factory('UserRoleSearch', function ($resource) {
        return $resource('api/_search/userRoles/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
