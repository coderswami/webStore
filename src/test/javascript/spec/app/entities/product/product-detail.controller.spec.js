'use strict';

describe('Controller Tests', function() {

    describe('Product Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProduct, MockCategory, MockProductAttr, MockProductPrice, MockProductReview;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProduct = jasmine.createSpy('MockProduct');
            MockCategory = jasmine.createSpy('MockCategory');
            MockProductAttr = jasmine.createSpy('MockProductAttr');
            MockProductPrice = jasmine.createSpy('MockProductPrice');
            MockProductReview = jasmine.createSpy('MockProductReview');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Product': MockProduct,
                'Category': MockCategory,
                'ProductAttr': MockProductAttr,
                'ProductPrice': MockProductPrice,
                'ProductReview': MockProductReview
            };
            createController = function() {
                $injector.get('$controller')("ProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:productUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
