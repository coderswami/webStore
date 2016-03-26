'use strict';

angular.module('webstoreApp')
    .factory('UserProfileSearch', function ($resource) {
        return $resource('api/_search/userProfiles/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
