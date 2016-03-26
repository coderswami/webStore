'use strict';

describe('Controller Tests', function() {

    describe('UserProfile Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserProfile, MockUserLogin, MockUserRole, MockOrderHeader, MockUserAddress;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockUserLogin = jasmine.createSpy('MockUserLogin');
            MockUserRole = jasmine.createSpy('MockUserRole');
            MockOrderHeader = jasmine.createSpy('MockOrderHeader');
            MockUserAddress = jasmine.createSpy('MockUserAddress');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserProfile': MockUserProfile,
                'UserLogin': MockUserLogin,
                'UserRole': MockUserRole,
                'OrderHeader': MockOrderHeader,
                'UserAddress': MockUserAddress
            };
            createController = function() {
                $injector.get('$controller')("UserProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:userProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
