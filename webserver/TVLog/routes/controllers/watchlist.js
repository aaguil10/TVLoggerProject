var express = require('express');
var request = require('request');
var router = express.Router();

var api_key = "36a849335545abdd3f76d463d4e0136aa8a9df97c3d4ef7ac2058ebc9bdd9c5c";
var access_token = "Bearer ae05b0f9b058b2ec3ba9e177bb6e4017249ce2301fd9499067096ebe9acac0ca";

var bodyParser = require('body-parser');
router.use(bodyParser.json() );       // to support JSON-encoded bodies
router.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
}));

router.get('/getWatchList', function(req, res, next) {
  console.log("Called getWatchList!");
  request({
    method: 'GET',
    url: 'https://api.trakt.tv/sync/watchlist/type',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': access_token,
      'trakt-api-version': '2',
      'trakt-api-key': api_key
    }}, function (error, response, body) {
    console.log('Status:', response.statusCode);
    console.log('Headers:', JSON.stringify(response.headers));
    console.log('Response:', body);
    res.send(body);
  });
});

router.post('/addToWatchList', function(req, res, next) {
   var m_id = req.body.movie;
  request({
    method: 'POST',
    url: 'https://api.trakt.tv/sync/watchlist',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': access_token,
      'trakt-api-version': '2',
      'trakt-api-key': api_key
    },
    body:m_id
  }, function (error, response, body) {
    // console.log('Status:', response.statusCode);
    // console.log('Headers:', JSON.stringify(response.headers));
    // console.log('Response:', body);
    res.send(body);
  });
});


router.post('/removeFromWatchList', function(req, res, next) {
  var m_id = req.body.movie;
  request({
    method: 'POST',
    url: 'https://api.trakt.tv/sync/watchlist/remove',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': access_token,
      'trakt-api-version': '2',
      'trakt-api-key': api_key
    },
    body: m_id
  }, function (error, response, body) {
    console.log('Status:', response.statusCode);
    console.log('Headers:', JSON.stringify(response.headers));
    console.log('Response:', body);
    res.send(body);
  });
});



router.get('/getWatched', function(req, res, next) {
  request({
    method: 'GET',
    url: 'https://api.trakt.tv/sync/history/type/id',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': access_token,
      'trakt-api-version': '2',
      'trakt-api-key': api_key
    }}, function (error, response, body) {
    // console.log('Status:', response.statusCode);
    // console.log('Headers:', JSON.stringify(response.headers));
    // console.log('Response:', body);
    res.send(body);
  });
});


module.exports = router;
