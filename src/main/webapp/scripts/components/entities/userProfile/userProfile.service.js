'use strict';

angular.module('webstoreApp')
    .factory('UserProfile', function ($resource, DateUtils) {
        return $resource('api/userProfiles/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dob = DateUtils.convertLocaleDateFromServer(data.dob);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dob = DateUtils.convertLocaleDateToServer(data.dob);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dob = DateUtils.convertLocaleDateToServer(data.dob);
                    return angular.toJson(data);
                }
            }
        });
    });
