(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ValoracionController', ValoracionController);

    ValoracionController.$inject = ['Valoracion'];

    function ValoracionController(Valoracion) {

        var vm = this;

        vm.valoracions = [];

        loadAll();

        function loadAll() {
            Valoracion.query(function(result) {
                vm.valoracions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
