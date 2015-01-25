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

// load the reagent app
var srcFile = "target/public/static/scripts/app.js";
var cljsLoad = require("./cljs-load");
cljsLoad.load(srcFile);

// ----------------------------------------------------------------------

// load the express framework
var express = require('express');
var st = require('st');
var app = express();
var timestamp = new Date().getMilliseconds().toString();

app.get('/', function (req, res) {
    res.send(site.tools.render_page(req.path, timestamp));
});

app.get('/lessons/*', function (req, res) {
    res.send(site.tools.render_page(req.path, timestamp));
});

var mount = st({path: 'target/public', url: '/'});
app.use(mount);


port = process.env.PORT || "3000";
app.listen(parseInt(port));
