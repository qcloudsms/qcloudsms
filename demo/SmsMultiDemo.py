#! /usr/bin/env python2
# encoding:utf-8
# python 2.7 测试通过
# python 3 更换适当的开发库就能使用，在此我们不额外提供

import httplib
import json
import hashlib
import random

class SmsMultiSender:
    sdkappid = 0
    appkey = ""
    # 请根据我们的开发文档适时调整 url
    url = "https://yun.tim.qq.com/v3/tlssmssvr/sendmultisms2"
    
    def __init__(self, sdkappid, appkey):
        self.sdkappid = sdkappid
        self.appkey = appkey
        
    def sendMsg(self, nationCode, phoneNumbers, content):
        """国家码，手机号和内容"""
        sig = self.calculateSig(self.appkey, phoneNumbers)
        tel = [ ]
        for phoneNumber in phoneNumbers:
            telElement = { "nationcode": nationCode, "phone": phoneNumber }
            tel.append(telElement);
            
        data = { }    
        data["tel"] = tel
        data["type"] = "0"
        data["msg"] = content
        data["sig"] = sig
        data["extend"] = ""
        data["ext"] = ""

        print json.dumps(data)

        con = None
        try:
            con = httplib.HTTPSConnection('yun.tim.qq.com', timeout=10)
            body = json.dumps(data)
            rnd = random.randint(100000, 999999)
            wholeUrl = '%s?sdkappid=%d&random=%d' % (self.url, self.sdkappid, rnd)
            con.request('POST', wholeUrl, body)
            response = con.getresponse()
            print response.status,response.reason
            rsp = response.read()
            print rsp
        except Exception,e:
            print e
        finally:
            if(con):
                con.close()

    def calculateSig(self, appkey, phoneNumbers):
        """计算 sig"""
        str = appkey + phoneNumbers[0]
        for i in range(1, len(phoneNumbers)):
            str = str + "," + phoneNumbers[i]
        return hashlib.md5(str).hexdigest()

if __name__ == "__main__":
    # 开放者实际发送短信时请使用申请的 sdkappid 和 appkey
    sender = SmsMultiSender(1234567890, "1234567890")
    # 下列手机号码均不存在，请替换成实际存在的
    phoneNumbers = [ "12345678901", "12345678902", "12345678903" ]
    # 请确保签名和模板审核通过
    sender.sendMsg("86", phoneNumbers, "验证码 1234")
