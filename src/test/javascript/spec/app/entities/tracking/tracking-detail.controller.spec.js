'use strict';

describe('Controller Tests', function() {

    describe('Tracking Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTracking, MockOrderHeader;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTracking = jasmine.createSpy('MockTracking');
            MockOrderHeader = jasmine.createSpy('MockOrderHeader');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tracking': MockTracking,
                'OrderHeader': MockOrderHeader
            };
            createController = function() {
                $injector.get('$controller')("TrackingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:trackingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
