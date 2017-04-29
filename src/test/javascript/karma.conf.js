// Karma configuration
// http://karma-runner.github.io/0.13/config/configuration-file.html

var sourcePreprocessors = ['coverage'];

function isDebug() {
    return process.argv.indexOf('--debug') >= 0;
}

if (isDebug()) {
    // Disable JS minification if Karma is run with debug option.
    sourcePreprocessors = [];
}

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g, '..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            'src/main/webappconoche/bower_components/jquery/dist/jquery.js',
            'src/main/webappconoche/app/aa-files/custom6-bootstrap.js',
            'src/main/webappconoche/app/aa-files/custom5-bootstrap-hover-dropdown.min.js',
            'src/main/webappconoche/app/aa-files/custom4-metronic.js',
            'src/main/webappconoche/app/aa-files/custom3-layout.js',
            'src/main/webappconoche/app/aa-files/custom2-demo.js',
            'src/main/webappconoche/app/aa-files/custom1-quick-sidebar.js',
            'src/main/webappconoche/app/aa-files/custom0-moment.js.js',

            // bower:js
            'src/main/webappconoche/bower_components/jquery/dist/jquery.js',
            'src/main/webappconoche/bower_components/messageformat/messageformat.js',
            'src/main/webappconoche/bower_components/json3/lib/json3.js',
            'src/main/webappconoche/bower_components/lodash/lodash.js',
            'src/main/webappconoche/bower_components/sockjs-client/dist/sockjs.js',
            'src/main/webappconoche/bower_components/stomp-websocket/lib/stomp.min.js',
            'src/main/webappconoche/bower_components/cloudinary-core/cloudinary-core.js',
            'src/main/webappconoche/bower_components/jquery-bridget/jquery-bridget.js',
            'src/main/webappconoche/bower_components/get-size/get-size.js',
            'src/main/webappconoche/bower_components/ev-emitter/ev-emitter.js',
            'src/main/webappconoche/bower_components/desandro-matches-selector/matches-selector.js',
            'src/main/webappconoche/bower_components/imagesloaded/imagesloaded.js',
            'src/main/webappconoche/bower_components/slimScroll/jquery.slimscroll.js',
            'src/main/webappconoche/bower_components/slimScroll/jquery.slimscroll.min.js',
            'src/main/webappconoche/bower_components/moment/moment.js',
            'src/main/webappconoche/bower_components/humanize-duration/humanize-duration.js',
            'src/main/webappconoche/bower_components/angular/angular.js',
            'src/main/webappconoche/bower_components/angular-aria/angular-aria.js',
            'src/main/webappconoche/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/webappconoche/bower_components/angular-cache-buster/angular-cache-buster.js',
            'src/main/webappconoche/bower_components/angular-cookies/angular-cookies.js',
            'src/main/webappconoche/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'src/main/webappconoche/bower_components/ngstorage/ngStorage.js',
            'src/main/webappconoche/bower_components/angular-loading-bar/build/loading-bar.js',
            'src/main/webappconoche/bower_components/angular-resource/angular-resource.js',
            'src/main/webappconoche/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/webappconoche/bower_components/angular-translate/angular-translate.js',
            'src/main/webappconoche/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'src/main/webappconoche/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'src/main/webappconoche/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'src/main/webappconoche/bower_components/angular-ui-router/release/angular-ui-router.js',
            'src/main/webappconoche/bower_components/bootstrap-ui-datetime-picker/dist/datetime-picker.js',
            'src/main/webappconoche/bower_components/ng-file-upload/ng-file-upload.js',
            'src/main/webappconoche/bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'src/main/webappconoche/bower_components/cloudinary_ng/js/angular.cloudinary.js',
            'src/main/webappconoche/bower_components/fizzy-ui-utils/utils.js',
            'src/main/webappconoche/bower_components/angular-animate/angular-animate.js',
            'src/main/webappconoche/bower_components/angular-slimscroll/angular-slimscroll.js',
            'src/main/webappconoche/bower_components/angular-timer/dist/angular-timer.js',
            'src/main/webappconoche/bower_components/angular-mocks/angular-mocks.js',
            'src/main/webappconoche/bower_components/outlayer/item.js',
            'src/main/webappconoche/bower_components/outlayer/outlayer.js',
            'src/main/webappconoche/bower_components/masonry/masonry.js',
            'src/main/webappconoche/bower_components/angular-masonry/angular-masonry.js',
            // endbower


            'src/main/webappconoche/app/app.module.js',
            'src/main/webappconoche/app/app.state.js',
            'src/main/webappconoche/app/app.constants.js',
            'src/main/webappconoche/app/services/cloudinary/*.js',
            'src/main/webappconoche/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: ['src/main/webappconoche/app/aa-files/custom3-layout.js'],

        preprocessors: {
            './**/*.js': sourcePreprocessors
        },

        reporters: ['dots', 'junit', 'coverage', 'progress'],

        junitReporter: {
            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            dir: 'target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout: 10000, // default 2000
        browserDisconnectTolerance: 1, // default 0
        browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
    });
};
