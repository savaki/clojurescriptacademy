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
var express = require('express'),
    st = require('st'),
    path = require('path');

var app = express();
var timestamp = new Date().getMilliseconds().toString();
var render_page = site.tools.render_page;

app.get('/', function (req, res) {
    res.send(render_page(req.path, timestamp));
});

app.get('/lessons/*', function (req, res) {
    res.send(render_page(req.path, timestamp));
});

var docroot = process.env.DOCROOT || '../dev';
var mount = st({path: docroot, url: '/'});
app.use(mount);


var port = process.env.PORT || "3000";
app.listen(parseInt(port));
