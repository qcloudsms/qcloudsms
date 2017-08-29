// 确保网络畅通的情况下运行，安装依赖
// npm install request

var sender = require('./SmsSender');

// 使用时请更换相应的 sdkappid 和 appkey。
// 创建sdkappid,appkey,签名模版等请参考接入指南：https://www.qcloud.com/document/product/382/3785
sender.config.sdkappid = 12345678;
sender.config.appkey = '1234567890efabc90888665';

var phoneNumbers = ['15800000000', '18600000000'];

sender.singleSmsSend(0, '86', phoneNumbers[0], '您已成功收到来自小明的1元转账，请登录钱包进行查看。', '', '', function (data) {
    var ret = JSON.parse(data);
    if (0 != ret.result) {
        console.log(ret);
    }
});

sender.singleSmsSendWithParam('86', phoneNumbers[0], 184, ['小明','1'], '', '', '', function (data) {
    var ret = JSON.parse(data);
    if (0 != ret.result) {
        console.log(ret);
    }
});

sender.multiSmsSend(0, '86', phoneNumbers, '您已成功收到来自小明的1元转账，请登录钱包进行查看。', '', '', function (data) {
    var ret = JSON.parse(data);
    if (0 != ret.result) {
        console.log(ret);
    }
});

sender.multiSmsSendWithParam('86', phoneNumbers, 184, ['小明','1'], '', '', '', function (data) {
    var ret = JSON.parse(data);
    if (0 != ret.result) {
        console.log(ret);
    }
});