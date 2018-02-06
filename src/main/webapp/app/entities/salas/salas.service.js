(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('Salas', Salas);

    Salas.$inject = ['$resource'];

    function Salas ($resource) {
        var resourceUrl =  'api/salas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
