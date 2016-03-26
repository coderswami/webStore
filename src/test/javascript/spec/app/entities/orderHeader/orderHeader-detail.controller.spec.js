'use strict';

describe('Controller Tests', function() {

    describe('OrderHeader Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrderHeader, MockUserProfile, MockOrderItem, MockPayment, MockShipment, MockTracking;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrderHeader = jasmine.createSpy('MockOrderHeader');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockOrderItem = jasmine.createSpy('MockOrderItem');
            MockPayment = jasmine.createSpy('MockPayment');
            MockShipment = jasmine.createSpy('MockShipment');
            MockTracking = jasmine.createSpy('MockTracking');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OrderHeader': MockOrderHeader,
                'UserProfile': MockUserProfile,
                'OrderItem': MockOrderItem,
                'Payment': MockPayment,
                'Shipment': MockShipment,
                'Tracking': MockTracking
            };
            createController = function() {
                $injector.get('$controller')("OrderHeaderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:orderHeaderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
