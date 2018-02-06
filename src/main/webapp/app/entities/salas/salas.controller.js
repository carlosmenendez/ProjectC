(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('SalasController', SalasController);

    SalasController.$inject = ['Salas'];

    function SalasController(Salas) {

        var vm = this;

        vm.salas = [];

        loadAll();

        function loadAll() {
            Salas.query(function(result) {
                vm.salas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
