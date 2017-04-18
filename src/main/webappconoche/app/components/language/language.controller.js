(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('JhiLanguageController', JhiLanguageController);

    JhiLanguageController.$inject = ['$translate', 'JhiLanguageService', 'tmhDynamicLocale','Principal'];

    function JhiLanguageController ($translate, JhiLanguageService, tmhDynamicLocale,Principal) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.currentLanguage = currentLanguage;
        vm.languages = null;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function currentLanguage() {
            return $translate.use();
        }

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
        }
    }
})();
