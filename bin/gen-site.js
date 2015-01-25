var cljsLoad = require("./cljs-load");

var srcFile = "target/public/index/app.js";

var beep = "\u0007";

if (typeof location === "undefined") {
    // figwheel wants js/location to exist, even if it doesn't run,
    // for some reason
    global.location = {};
}

var gensite = function () {
    console.log("Loading " + srcFile);
    var optNone = cljsLoad.load(srcFile);
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
    // fake out objects we expect to exist
    global.React = require("./react-0.12.2.js");
    global.window = {
        attachEvent: function (eventName, callback) {
            return {
                pathname: "/"
            };
        },
        location: {
            pathname: "/"
        }
    };
    global.document = {
        attachEvent: function (eventName, callback) {
            return {
                pathname: "/"
            };
        },
        location: {
            pathname: "/"
        }
    };

    try {
        gensite();
    } catch (e) {
        console.log(e + beep);
        console.error(e.stack);
    }
}
