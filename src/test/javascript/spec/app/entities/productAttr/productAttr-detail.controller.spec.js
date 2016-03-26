'use strict';

describe('Controller Tests', function() {

    describe('ProductAttr Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductAttr, MockProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductAttr = jasmine.createSpy('MockProductAttr');
            MockProduct = jasmine.createSpy('MockProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductAttr': MockProductAttr,
                'Product': MockProduct
            };
            createController = function() {
                $injector.get('$controller')("ProductAttrDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:productAttrUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
