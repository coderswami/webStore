 'use strict';

angular.module('webstoreApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-webstoreApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-webstoreApp-params')});
                }
                return response;
            }
        };
    });
