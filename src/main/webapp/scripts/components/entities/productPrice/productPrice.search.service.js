'use strict';

angular.module('webstoreApp')
    .factory('ProductPriceSearch', function ($resource) {
        return $resource('api/_search/productPrices/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
