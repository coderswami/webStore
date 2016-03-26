'use strict';

describe('Controller Tests', function() {

    describe('Catalog Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCatalog, MockCountry, MockCategory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCatalog = jasmine.createSpy('MockCatalog');
            MockCountry = jasmine.createSpy('MockCountry');
            MockCategory = jasmine.createSpy('MockCategory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Catalog': MockCatalog,
                'Country': MockCountry,
                'Category': MockCategory
            };
            createController = function() {
                $injector.get('$controller')("CatalogDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:catalogUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
