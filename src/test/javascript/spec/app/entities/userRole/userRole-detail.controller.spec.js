'use strict';

describe('Controller Tests', function() {

    describe('UserRole Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserRole, MockUserProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserRole = jasmine.createSpy('MockUserRole');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserRole': MockUserRole,
                'UserProfile': MockUserProfile
            };
            createController = function() {
                $injector.get('$controller')("UserRoleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:userRoleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
