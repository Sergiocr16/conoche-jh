(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('JhiLanguageController', JhiLanguageController);

    JhiLanguageController.$inject = ['$translate', 'JhiLanguageService', 'tmhDynamicLocale','Principal'];

    function JhiLanguageController ($translate, JhiLanguageService, tmhDynamicLocale,Principal) {
        var vm = this;
        Principal.identity().then(function(account) {
            vm.currentLanguage = account.langKey;
        });
        console.log(vm.account);
        vm.changeLanguage = changeLanguage;
        vm.languages = null;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            vm.currentLanguage = languageKey;
        }
    }
})();
