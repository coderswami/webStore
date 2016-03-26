'use strict';

angular.module('webstoreApp')
    .factory('StateSearch', function ($resource) {
        return $resource('api/_search/states/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
