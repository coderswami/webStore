'use strict';

angular.module('webstoreApp')
    .factory('ProductReview', function ($resource, DateUtils) {
        return $resource('api/productReviews/:id', {}, {
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
