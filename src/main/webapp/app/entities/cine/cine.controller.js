(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CineController', CineController);

    CineController.$inject = ['Cine'];

    function CineController(Cine) {

        var vm = this;

        vm.cines = [];

        loadAll();

        function loadAll() {
            Cine.query(function(result) {
                vm.cines = result;
                vm.searchQuery = null;
            });
        }
    }
})();
