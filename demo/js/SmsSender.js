var crypto = require('crypto');
var request = require('request');

var config = { sdkappid: '', appkey: '' };

var singleSmsUrl = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";
var multiSmsUrl = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2";

function getSmsSig(rand, curTime, phoneNumbers) {
    var phoneNumberStr = phoneNumbers[0];
    for (var i = 1; i < phoneNumbers.length; i++) {
        phoneNumberStr += (','+phoneNumbers[i]);
    }

    return crypto.createHash('sha256').update('appkey='+config.appkey+'&random='+rand+'&time='+curTime+'&mobile='+phoneNumberStr).digest('hex');
}

function singleSmsSend(msgType, nationCode, phoneNumber, msg, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var reqObj = {
        tel: {
            nationCode: nationCode+'',
            mobile: phoneNumber+'',
        },
        type: Number(msgType),
        msg: msg,
        sig: getSmsSig(rand, curTime, [phoneNumber]),
        time: curTime,
        extend: extend,
        ext: ext
    };

    function callback(error, response, body) {
        if (!error && response.statusCode == 200) {
            cb(body);
        } else if (!error && response.statusCode != 200) {
            retObj = {
                result: -1,
                errmsg: error.toString() + ' http code ' + response.statusCode
            }
            cb(JSON.stringify(retObj));
        } else {
            var retObj = {
                result: -2,
                errmsg: error.toString()
            };
            cb(JSON.stringify(retObj));
        }
    }

    request({
        url: singleSmsUrl + '?sdkappid=' + config.sdkappid + '&random=' + rand,
        method: 'POST',
        json: false,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

function singleSmsSendWithParam(nationCode, phoneNumber, templId, params, sign, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var reqObj = {
        tel: {
            nationCode: nationCode+'',
            mobile: phoneNumber+'',
        },
        sign: sign,
        tpl_id: Number(templId),
        params: params,
        sig: getSmsSig(rand, curTime, [phoneNumber]),
        time: curTime,
        extend: extend,
        ext: ext
    };

    function callback(error, response, body) {
        if (!error && response.statusCode == 200) {
            cb(body);
        } else if (!error && response.statusCode != 200) {
            retObj = {
                result: -1,
                errmsg: error.toString() + ' http code ' + response.statusCode
            }
            cb(JSON.stringify(retObj));
        } else {
            var retObj = {
                result: -2,
                errmsg: error.toString()
            };
            cb(JSON.stringify(retObj));
        }
    }

    request({
        url: singleSmsUrl + '?sdkappid=' + config.sdkappid + '&random=' + rand,
        method: 'POST',
        json: false,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

function multiSmsSend(msgType, nationCode, phoneNumbers, msg, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var tel = [];
    for (var i in phoneNumbers) {
        tel.push({ nationcode: nationCode, mobile: phoneNumbers[i] });
    }

    var reqObj = {
        tel: tel,
        type: Number(type),
        msg: msg,
        sig: getSmsSig(rand, curTime, phoneNumbers),
        time: curTime,
        extend: extend,
        ext: ext
    };

    function callback(error, response, body) {
        if (!error && response.statusCode == 200) {
            cb(body);
        } else if (!error && response.statusCode != 200) {
            retObj = {
                result: -1,
                errmsg: error.toString() + ' http code ' + response.statusCode
            };
            cb(JSON.stringify(retObj));
        } else {
            var retObj = {
                result: -2,
                errmsg: error.toString()
            };
            cb(JSON.stringify(retObj));
        }
    }

    request({
        url: multiSmsUrl + '?sdkappid=' + config.sdkappid + '&random=' + rand,
        method: 'POST',
        json: false,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

function multiSmsSendWithParam(nationCode, phoneNumbers, templId, params, sign, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var tel = [];
    for (var i in phoneNumbers) {
        tel.push({ nationcode: nationCode, mobile: phoneNumbers[i] });
    }

    var reqObj = {
        tel: tel,
        sign: sign,
        tpl_id: Number(templId),
        params: params,
        sig: getSmsSig(rand, curTime, phoneNumbers),
        time: curTime,
        extend: extend,
        ext: ext
    };

    function callback(error, response, body) {
        if (!error && response.statusCode == 200) {
            cb(body);
        } else if (!error && response.statusCode != 200) {
            retObj = {
                result: -1,
                errmsg: error.toString() + ' http code ' + response.statusCode
            };
            cb(JSON.stringify(retObj));
        } else {
            var retObj = {
                result: -2,
                errmsg: error.toString()
            };
            cb(JSON.stringify(retObj));
        }
    }

    request({
        url: multiSmsUrl + '?sdkappid=' + config.sdkappid + '&random=' + rand,
        method: 'POST',
        json: false,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

exports.singleSmsSend = singleSmsSend;
exports.singleSmsSendWithParam = singleSmsSendWithParam;
exports.multiSmsSend = multiSmsSend;
exports.multiSmsSendWithParam = multiSmsSendWithParam;

exports.config = config;