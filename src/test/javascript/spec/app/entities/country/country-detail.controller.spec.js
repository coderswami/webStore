'use strict';

describe('Controller Tests', function() {

    describe('Country Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCountry, MockState, MockCatalog;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCountry = jasmine.createSpy('MockCountry');
            MockState = jasmine.createSpy('MockState');
            MockCatalog = jasmine.createSpy('MockCatalog');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Country': MockCountry,
                'State': MockState,
                'Catalog': MockCatalog
            };
            createController = function() {
                $injector.get('$controller')("CountryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:countryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
