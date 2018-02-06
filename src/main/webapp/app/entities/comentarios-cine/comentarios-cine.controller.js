(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosCineController', ComentariosCineController);

    ComentariosCineController.$inject = ['ComentariosCine'];

    function ComentariosCineController(ComentariosCine) {

        var vm = this;

        vm.comentariosCines = [];

        loadAll();

        function loadAll() {
            ComentariosCine.query(function(result) {
                vm.comentariosCines = result;
                vm.searchQuery = null;
            });
        }
    }
})();
