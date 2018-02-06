(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CarteleraController', CarteleraController);

    CarteleraController.$inject = ['Cartelera'];

    function CarteleraController(Cartelera) {

        var vm = this;

        vm.carteleras = [];

        loadAll();

        function loadAll() {
            Cartelera.query(function(result) {
                vm.carteleras = result;
                vm.searchQuery = null;
            });
        }
    }
})();
