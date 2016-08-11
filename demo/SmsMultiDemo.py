#! /usr/bin/env python2
# encoding:utf-8
# python 2.7 测试通过
# python 3 更换适当的开发库就能使用，在此我们不额外提供

import httplib
import json
import hashlib
import random

class SmsSender:
    sdkappid = 0
    appkey = ""
    # 请根据我们的开发文档适时调整 url
    url = "https://yun.tim.qq.com/v3/tlssmssvr/sendsms"
    
    def __init__(self, sdkappid, appkey):
        self.sdkappid = sdkappid
        self.appkey = appkey
        
    def sendMsg(self, nationCode, phoneNumber, content):
        """国家码，手机号和内容"""
        sig = hashlib.md5(self.appkey+phoneNumber).hexdigest()
        pkg = {
            "tel": {
                "nationcode": nationCode,
                "phone": phoneNumber
            },
            "type": "0",
            "msg": content,
            "sig": sig,
            "extend": "",
            "ext":"123"
        }
        
        con = None
        try:
            con = httplib.HTTPSConnection('yun.tim.qq.com', timeout=10)
            body = json.dumps(pkg)
            rnd = random.randint(100000, 999999)
            wholeUrl = '%s?sdkappid=%d&random=%d' % (self.url, self.sdkappid, rnd)
            con.request('POST', wholeUrl, body)
            response = con.getresponse()
            print response.status,response.reason
            data = response.read()
            print data
        except Exception,e:
            print e
        finally:
            if(con):
                con.close()

if __name__ == "__main__":
    # 开放者实际发送短信时请使用申请的 sdkappid 和 appkey
    sender = SmsSender(1234567890, "1234567890")
    sender.sendMsg("86", "13012345678", "1234")
