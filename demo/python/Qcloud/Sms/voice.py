#! /usr/bin/env python2
# encoding:utf-8

import hashlib
from tools import SmsSenderUtil

'''语音验证码发送'''
class VoiceSender:
    appid = 0
    appkey = 'default_key'
    url = "v5/tlsvoicesvr/sendvoice"
    def __init__(self, appid, appkey):
        self.appid = appid
        self.appkey = appkey
        self.util = SmsSenderUtil()

    """ 语音验证码发送
    Returns:
        请求包体
        {
            "tel": {
                "nationcode": "86", //国家码
                "mobile": "13788888888" //手机号码
            },
            "msg": "1234", //验证码，支持英文字母、数字及组合；实际发送给用户时，语音验证码内容前会添加"您的验证码是"语音提示。
            "playtimes": 2, //播放次数，可选，最多3次，默认2次
            "sig": "30db206bfd3fea7ef0db929998642c8ea54cc7042a779c5a0d9897358f6e9505", //app凭证，具体计算方式见下注
            "time": 1457336869, //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会返回失败
            "ext": "" //用户的session内容，腾讯server回包中会原样返回，可选字段，不需要就填空。
        }
        应答包体
        {
            "result": 0, //0表示成功，非0表示失败
            "errmsg": "OK", //result非0时的具体错误信息
            "ext": "", //用户的session内容，腾讯server回包中会原样返回
            "callid": "xxxx" //标识本次发送id，标识一次下发记录
        }
    参数说明:
        nation_code: 国家码，如 86 为中国
        phone_number: 不带国家码的手机号
        msg: 信息内容，必须与申请的模板格式一致，否则将返回错误
        ext: 服务端原样返回的参数，可填空串
    """
    def send(self,nation_code, phone_number,playtimes,msg, ext):
        rnd = self.util.get_random()
        cur_time = self.util.get_cur_time()

        data = {}
        tel = {"nationcode": nation_code, "mobile": phone_number}
        data["tel"] = tel
        data["msg"] = msg
        data["playtimes"] = playtimes
        data["sig"] = hashlib.sha256("appkey=" + self.appkey + "&random=" + str(rnd)
                                     + "&time=" + str(cur_time) + "&mobile=" + phone_number).hexdigest()
        data["time"] = cur_time
        data["ext"] = ext

        whole_url = self.url + "?sdkappid=" + str(self.appid) + "&random=" + str(rnd)
        return self.util.send_post_request("yun.tim.qq.com", whole_url, data)

'''语音通知发送'''
class VoicePromptSender:
    appid = 0
    appkey = 'default_key'
    url = "/v5/tlsvoicesvr/sendvoiceprompt"
    def __init__(self, appid, appkey):
        self.appid = appid
        self.appkey = appkey
        self.util = SmsSenderUtil()

    """ 语音验证码发送
    Returns:
        请求包体
        {
            "tel": {
                "nationcode": "86", //国家码
                "mobile": "13788888888" //手机号码
            },
            "prompttype": 2, //语音类型，目前固定为2
            "promptfile": "语音内容文本", //通知内容，utf8编码，支持中文英文、数字及组合，需要和语音内容模版相匹配
            "playtimes": 2, //播放次数，可选，最多3次，默认2次
            "sig": "30db206bfd3fea7ef0db929998642c8ea54cc7042a779c5a0d9897358f6e9505", //app凭证，具体计算方式见下注
            "time": 1457336869, //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会返回失败
            "ext": "" //用户的session内容，腾讯server回包中会原样返回，可选字段，不需要就填空。
        }
        应答包体
        {
            "result": 0, //0表示成功，非0表示失败
            "errmsg": "OK", //result非0时的具体错误信息
            "ext": "", //用户的session内容，腾讯server回包中会原样返回
            "callid": "xxxx" //标识本次发送id，标识一次下发记录
        }
    参数说明:
        nation_code: 国家码，如 86 为中国
        phone_number: 不带国家码的手机号
        msg: 信息内容，必须与申请的模板格式一致，否则将返回错误
        ext: 服务端原样返回的参数，可填空串
    """
    def send(self,nation_code, phone_number,playtimes,msg, ext):
        rnd = self.util.get_random()
        cur_time = self.util.get_cur_time()

        data = {}
        tel = {"nationcode": nation_code, "mobile": phone_number}
        data["tel"] = tel
        data["prompttype"] = 2
        data["promptfile"] = msg
        data["playtimes"] = playtimes
        data["sig"] = hashlib.sha256("appkey=" + self.appkey + "&random=" + str(rnd)
                                     + "&time=" + str(cur_time) + "&mobile=" + phone_number).hexdigest()
        data["time"] = cur_time
        data["ext"] = ext

        whole_url = self.url + "?sdkappid=" + str(self.appid) + "&random=" + str(rnd)
        return self.util.send_post_request("yun.tim.qq.com", whole_url, data)