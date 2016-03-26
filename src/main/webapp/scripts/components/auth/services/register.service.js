'use strict';

angular.module('webstoreApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


