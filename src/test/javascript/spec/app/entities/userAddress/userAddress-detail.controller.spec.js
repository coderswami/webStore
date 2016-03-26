'use strict';

describe('Controller Tests', function() {

    describe('UserAddress Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserAddress, MockUserProfile, MockCountry, MockState;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserAddress = jasmine.createSpy('MockUserAddress');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockCountry = jasmine.createSpy('MockCountry');
            MockState = jasmine.createSpy('MockState');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserAddress': MockUserAddress,
                'UserProfile': MockUserProfile,
                'Country': MockCountry,
                'State': MockState
            };
            createController = function() {
                $injector.get('$controller')("UserAddressDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:userAddressUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
