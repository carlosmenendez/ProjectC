(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RepartoController', RepartoController);

    RepartoController.$inject = ['Reparto'];

    function RepartoController(Reparto) {

        var vm = this;

        vm.repartos = [];

        loadAll();

        function loadAll() {
            Reparto.query(function(result) {
                vm.repartos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
