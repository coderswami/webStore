'use strict';

angular.module('webstoreApp')
    .factory('UserAddressSearch', function ($resource) {
        return $resource('api/_search/userAddresss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
