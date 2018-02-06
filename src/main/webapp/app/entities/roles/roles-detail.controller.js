(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RolesDetailController', RolesDetailController);

    RolesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Roles'];

    function RolesDetailController($scope, $rootScope, $stateParams, previousState, entity, Roles) {
        var vm = this;

        vm.roles = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:rolesUpdate', function(event, result) {
            vm.roles = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
