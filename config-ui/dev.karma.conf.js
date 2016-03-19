// Karma configuration
// Generated on Wed Jun 04 2014 17:09:36 GMT-0700 (Pacific Daylight Time)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '../',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
	  'config-ui/bower_components/angular/angular.js',
      'config-ui/bower_components/angular-resource/angular-resource.js',
      'config-ui/bower_components/angular-translate/angular-translate.js',
      'config-ui/bower_components/angular-translate-loader-url/angular-translate-loader-url.js',
      'config-ui/bower_components/jquery/dist/*.js',
      'config-ui/bower_components/angular-mocks/angular-mocks.js',
      'config-ui/bower_components/angular-bootstrap/*.js',
      'config-ui/bower_components/google-diff-match-patch-js/*.js',
      'config-ui/bower_components/angular-ui-router/**/angular-ui-router.js',
      'config-ui/bower_components/ngstorage/ngStorage.js',
      'config-ui/bower_components/angular-sanitize/angular-sanitize.js',
      'config-ui/test/mock/*.js',
	  'config-ui/src/**/*.js',
      'config-ui/src/base/app/app.js',
      'config-ui/test/unit/**/*.js'
    ],

    // list of files to exclude
    exclude: [
      
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['Chrome'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false,
    
    reporters: ['dots', 'junit'],
    
    junitReporter : {
    	outputFile: 'config-ui/build/reports/karma/test-results.xml'
    }
   
  });
};
