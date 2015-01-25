var fs = require("fs");
var vm = require("vm");
var path = require("path");

var loadSrc = function (mainFile) {
    console.log("reading file, " + mainFile);
    var src = fs.readFileSync(mainFile);

    global.globalNodeRequire = require;

    vm.runInThisContext("(function (require) {"
    + src
    + "\n})(globalNodeRequire);", mainFile);
    return false;
};

exports.load = loadSrc;
