'use strict';

angular.module('webstoreApp')
    .factory('ProductAttrSearch', function ($resource) {
        return $resource('api/_search/productAttrs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
