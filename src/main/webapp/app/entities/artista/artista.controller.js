(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ArtistaController', ArtistaController);

    ArtistaController.$inject = ['Artista'];

    function ArtistaController(Artista) {

        var vm = this;

        vm.artistas = [];

        loadAll();

        function loadAll() {
            Artista.query(function(result) {
                vm.artistas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
