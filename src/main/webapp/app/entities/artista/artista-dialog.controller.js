(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ArtistaDialogController', ArtistaDialogController);

    ArtistaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artista'];

    function ArtistaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Artista) {
        var vm = this;

        vm.artista = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.artista.id !== null) {
                Artista.update(vm.artista, onSaveSuccess, onSaveError);
            } else {
                Artista.save(vm.artista, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:artistaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaNacimiento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
