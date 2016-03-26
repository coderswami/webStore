'use strict';

describe('Controller Tests', function() {

    describe('ProductPrice Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductPrice, MockProduct, MockCurrency;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductPrice = jasmine.createSpy('MockProductPrice');
            MockProduct = jasmine.createSpy('MockProduct');
            MockCurrency = jasmine.createSpy('MockCurrency');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductPrice': MockProductPrice,
                'Product': MockProduct,
                'Currency': MockCurrency
            };
            createController = function() {
                $injector.get('$controller')("ProductPriceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:productPriceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
