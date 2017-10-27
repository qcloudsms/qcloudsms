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

    // 一定要使用 utf-8 编码
    return crypto.createHash('sha256').update('appkey='+config.appkey+'&random='+rand+'&time='+curTime+'&mobile='+phoneNumberStr, 'utf-8').digest('hex');
}

/**
 * 单发短信接口
 * @param {number} msgType 短信类型，0 普通短信，1 营销短信，请严格按照相应的类型填写
 * @param {string} nationCode 国家码，如果中国 86
 * @param {string} phoneNumber 手机号
 * @param {string} msg 短信正文，如果需要带签名，签名请使用【】标注
 * @param {string} extend 扩展字段，如无需要请填空字符串
 * @param {string} ext 此字段腾讯云后台服务器会按原样在应答中
 * @param {function} cb 异步结果回调函数
 */
function singleSmsSend(msgType, nationCode, phoneNumber, msg, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var reqObj = {
        tel: {
            nationcode: nationCode+'',
            mobile: phoneNumber+''
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
                errmsg: 'http code ' + response.statusCode
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
        url: singleSmsUrl + '?sdkappid=' + config.sdkappid + '&random=' + rand,
        method: 'POST',
        json: false,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

/**
 * 带模板单发短信接口
 * @param {string} nationCode 国家码，如果中国 86
 * @param {string} phoneNumber 手机号
 * @param {number} templId 短信模板参数，如不清楚，请在 https://console.qcloud.com/sms/smsContent 查看
 * @param {array} params 模板参数数组，元素类型为 {string}，元素个数请不要超过模板参数个数
 * @param {string} sign 短信签名
 * @param {string} extend 扩展字段，如无需要请填空字符串
 * @param {string} ext 此字段腾讯云后台服务器会按原样在应答中
 * @param {function} cb 异步结果回调函数
 */
function singleSmsSendWithParam(nationCode, phoneNumber, templId, params, sign, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var reqObj = {
        tel: {
            nationcode: nationCode+'',
            mobile: phoneNumber+''
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
                errmsg: 'http code ' + response.statusCode
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
        url: singleSmsUrl + '?sdkappid=' + config.sdkappid + '&random=' + rand,
        method: 'POST',
        json: false,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

/**
 * 群发短信接口
 * @param {number} msgType 短信类型，0 普通短信，1 营销短信，请严格按照相应的类型填写
 * @param {string} nationCode
 * @param {array} phoneNumbers 手机号数组，元素类型为 {string}
 * @param {string} msg 短信正文，如果需要带签名，签名请使用【】标注
 * @param {string} extend 扩展字段，如无需要请填空字符串
 * @param {string} ext 此字段腾讯云后台服务器会按原样在应答中，如不需要请填空字符
 * @param {function} cb 异步结果回调函数
 */
function multiSmsSend(msgType, nationCode, phoneNumbers, msg, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var tel = [];
    for (var i = 0; i < phoneNumbers.length; i++) {
        tel.push({ nationcode: nationCode, mobile: phoneNumbers[i] });
    }

    var reqObj = {
        tel: tel,
        type: Number(msgType),
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
                errmsg: 'http code ' + response.statusCode
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
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

/**
 * 带模板群发短信接口
 * @param {string} nationCode 国家码，如果中国 86
 * @param {array} phoneNumbers 手机号数组，元素类型为 {string}
 * @param {number} templId 短信模板参数，如不清楚，请在 https://console.qcloud.com/sms/smsContent 查看
 * @param {array} params 模板参数数组，元素类型为 {string}，元素个数请不要超过模板参数个数
 * @param {string} sign 短信签名
 * @param {string} extend 扩展字段，如无需要请填空字符串
 * @param {string} ext 此字段腾讯云后台服务器会按原样在应答中
 * @param {function} cb 异步结果回调函数
 */
function multiSmsSendWithParam(nationCode, phoneNumbers, templId, params, sign, extend, ext, cb) {

    var rand = Math.round(Math.random()*99999);
    var curTime = Math.floor(Date.now()/1000);

    var tel = [];
    for (var i = 0; i < phoneNumbers.length; i++) {
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
                errmsg: 'http code ' + response.statusCode
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
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reqObj)
    }, callback);
}

exports.singleSmsSend = singleSmsSend;
exports.singleSmsSendWithParam = singleSmsSendWithParam;
exports.multiSmsSend = multiSmsSend;
exports.multiSmsSendWithParam = multiSmsSendWithParam;

exports.config = config;