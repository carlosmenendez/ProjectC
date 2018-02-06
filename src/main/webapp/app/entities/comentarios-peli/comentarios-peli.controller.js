(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosPeliController', ComentariosPeliController);

    ComentariosPeliController.$inject = ['ComentariosPeli'];

    function ComentariosPeliController(ComentariosPeli) {

        var vm = this;

        vm.comentariosPelis = [];

        loadAll();

        function loadAll() {
            ComentariosPeli.query(function(result) {
                vm.comentariosPelis = result;
                vm.searchQuery = null;
            });
        }
    }
})();
