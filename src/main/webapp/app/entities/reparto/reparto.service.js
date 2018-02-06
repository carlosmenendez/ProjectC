(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('Reparto', Reparto);

    Reparto.$inject = ['$resource'];

    function Reparto ($resource) {
        var resourceUrl =  'api/repartos/:id';

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
