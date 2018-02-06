(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('Valoracion', Valoracion);

    Valoracion.$inject = ['$resource'];

    function Valoracion ($resource) {
        var resourceUrl =  'api/valoracions/:id';

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
