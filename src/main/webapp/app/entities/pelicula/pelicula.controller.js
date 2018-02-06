(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('PeliculaController', PeliculaController);

    PeliculaController.$inject = ['Pelicula'];

    function PeliculaController(Pelicula) {

        var vm = this;

        vm.peliculas = [];

        loadAll();

        function loadAll() {
            Pelicula.query(function(result) {
                vm.peliculas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
