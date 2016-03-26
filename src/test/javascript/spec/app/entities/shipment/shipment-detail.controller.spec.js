'use strict';

describe('Controller Tests', function() {

    describe('Shipment Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShipment, MockUserAddress;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShipment = jasmine.createSpy('MockShipment');
            MockUserAddress = jasmine.createSpy('MockUserAddress');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Shipment': MockShipment,
                'UserAddress': MockUserAddress
            };
            createController = function() {
                $injector.get('$controller')("ShipmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:shipmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
