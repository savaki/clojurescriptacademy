// ----------------------------------------------------------------------
// fake required browser elements

global.React = require("./react.js");
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

// ----------------------------------------------------------------------
// load the reagent app

require('./goog/bootstrap/nodejs');
require('./app');
goog.require('site.tools');

// ----------------------------------------------------------------------
// load the express framework

var express = require('express');
var st = require('st');
var app = express();
var render_page = site.tools.render_page;

var timestamp = Math.round(new Date() / 1000);
var advanced_mode = process.env.ADVANCED_MODE;

app.get('/', function (req, res) {
    res.send(render_page(req.path, timestamp, advanced_mode));
});

app.get('/lessons/*', function (req, res) {
    res.send(render_page(req.path, timestamp, advanced_mode));
});

var docroot = process.env.DOCROOT || '../dev';
var mount = st({path: docroot, url: '/'});
app.use(mount);

// ----------------------------------------------------------------------
// start the server

port = process.env.PORT || 3000;
app.listen(port);

