(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosCineDialogController', ComentariosCineDialogController);

    ComentariosCineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ComentariosCine', 'Cine', 'User'];

    function ComentariosCineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ComentariosCine, Cine, User) {
        var vm = this;

        vm.comentariosCine = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cines = Cine.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comentariosCine.id !== null) {
                ComentariosCine.update(vm.comentariosCine, onSaveSuccess, onSaveError);
            } else {
                ComentariosCine.save(vm.comentariosCine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:comentariosCineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
