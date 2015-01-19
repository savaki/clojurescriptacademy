
var cljsLoad = require("./cljs-load");

var srcFile         = "target/public/index/app.js";
var outputDirectory = "target/public/index/js/";

var beep = "\u0007";

if (typeof location === "undefined") {
    // figwheel wants js/location to exist, even if it doesn't run,
    // for some reason
    global.location = {};
}

var gensite = function () {
    console.log("Loading " + srcFile);
    var optNone = cljsLoad.load(srcFile, outputDirectory);
    sitetools.genpages({"opt-none": optNone});
}

var compileFail = function () {
  var msg = process.argv[process.argv.length - 1];
  if (msg && msg.match(/failed/)) {
    console.log("Compilation failed" + beep);
    return true;
  }
};

if (!compileFail()) {
  try {
    gensite();
  } catch (e) {
    console.log(e + beep);
    console.error(e.stack);
  }
}
