(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RolesController', RolesController);

    RolesController.$inject = ['Roles'];

    function RolesController(Roles) {

        var vm = this;

        vm.roles = [];

        loadAll();

        function loadAll() {
            Roles.query(function(result) {
                vm.roles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
