(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CarteleraDialogController', CarteleraDialogController);

    CarteleraDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cartelera', 'Pelicula', 'Cine'];

    function CarteleraDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cartelera, Pelicula, Cine) {
        var vm = this;

        vm.cartelera = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.peliculas = Pelicula.query();
        vm.cines = Cine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cartelera.id !== null) {
                Cartelera.update(vm.cartelera, onSaveSuccess, onSaveError);
            } else {
                Cartelera.save(vm.cartelera, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:carteleraUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dia = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
