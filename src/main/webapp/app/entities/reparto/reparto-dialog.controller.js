(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RepartoDialogController', RepartoDialogController);

    RepartoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Reparto', 'Artista', 'Pelicula', 'Roles'];

    function RepartoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Reparto, Artista, Pelicula, Roles) {
        var vm = this;

        vm.reparto = entity;
        vm.clear = clear;
        vm.save = save;
        vm.artistas = Artista.query();
        vm.peliculas = Pelicula.query();
        vm.roles = Roles.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reparto.id !== null) {
                Reparto.update(vm.reparto, onSaveSuccess, onSaveError);
            } else {
                Reparto.save(vm.reparto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:repartoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
