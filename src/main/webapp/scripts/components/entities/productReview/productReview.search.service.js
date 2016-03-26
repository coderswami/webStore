'use strict';

angular.module('webstoreApp')
    .factory('ProductReviewSearch', function ($resource) {
        return $resource('api/_search/productReviews/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
