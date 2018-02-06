(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('PeliculaDialogController', PeliculaDialogController);

    PeliculaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pelicula'];

    function PeliculaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pelicula) {
        var vm = this;

        vm.pelicula = entity;
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
            if (vm.pelicula.id !== null) {
                Pelicula.update(vm.pelicula, onSaveSuccess, onSaveError);
            } else {
                Pelicula.save(vm.pelicula, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:peliculaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaDeEstreno = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
