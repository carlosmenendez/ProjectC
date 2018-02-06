(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('ComentariosPeli', ComentariosPeli);

    ComentariosPeli.$inject = ['$resource'];

    function ComentariosPeli ($resource) {
        var resourceUrl =  'api/comentarios-pelis/:id';

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
