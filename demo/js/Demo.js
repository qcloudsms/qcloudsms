// 确保网络畅通的情况下运行，安装依赖
// npm install request

var sender = require('./SmsSender');

// 使用时请更换相应的 sdkappid 和 appkey
sender.config.sdkappid = 12345678;
sender.config.appkey = '1234567890efabc90888665';

sender.singleSmsSend(0, '86', '12345678901', '您已成功收到来自{1}的{2}元转账，请登录钱包进行查看。', '', '', function (ret) {
    console.log(ret);
});

sender.singleSmsSendWithParam('86', '12345678901', 184, ['小明','1'], '', '', '', function (ret) {
    console.log(ret);
});

sender.multiSmsSend(0, '86', ['12345678901', '12345678902'], '您已成功收到来自小明的1元转账，请登录钱包进行查看。', '', '', function (ret) {
    console.log(ret);
});

sender.multiSmsSendWithParam('86', ['12345678901', '12345678902'], 184, ['小明','1'], '', '', '', function (ret) {
    console.log(ret);
});