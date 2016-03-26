'use strict';

angular.module('webstoreApp')
    .factory('UserLoginSearch', function ($resource) {
        return $resource('api/_search/userLogins/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
