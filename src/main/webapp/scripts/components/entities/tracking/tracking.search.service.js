'use strict';

angular.module('webstoreApp')
    .factory('TrackingSearch', function ($resource) {
        return $resource('api/_search/trackings/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
