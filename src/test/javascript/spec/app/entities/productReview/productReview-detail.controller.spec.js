'use strict';

describe('Controller Tests', function() {

    describe('ProductReview Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductReview, MockProduct, MockUserProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductReview = jasmine.createSpy('MockProductReview');
            MockProduct = jasmine.createSpy('MockProduct');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductReview': MockProductReview,
                'Product': MockProduct,
                'UserProfile': MockUserProfile
            };
            createController = function() {
                $injector.get('$controller')("ProductReviewDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstoreApp:productReviewUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
