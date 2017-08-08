#! /usr/bin/env python2
# encoding:utf-8

import json
import Qcloud.Sms.sms as SmsSender
from Qcloud.Sms.voice import VoiceSender
from Qcloud.Sms.voice import VoicePromptSender

def sms():
    # 请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
    # appid,appkey,templId申请方式可参考接入指南https://www.qcloud.com/document/product/382/3785#5-.E7.9F.AD.E4.BF.A1.E5.86.85.E5.AE.B9.E9.85.8D.E7.BD.AE
    appid = 123456
    appkey = "1234567890abcdef1234567890abcdef"    
    phone_number1 = "12345678901"
    phone_number2 = "12345678902"
    phone_number3 = "12345678903"
    phone_numbers = [phone_number1, phone_number2, phone_number3]
    templ_id = 7839

    single_sender = SmsSender.SmsSingleSender(appid, appkey)

    # 普通单发
    result = single_sender.send(0, "86", phone_number2, "测试短信，普通单发，深圳，小明，上学。", "", "")
    rsp = json.loads(result)
    print result

    # 指定模板单发
    params = ["指定模板单发", "深圳", "小明"]
    result = single_sender.send_with_param("86", phone_number2, templ_id, params, "", "", "")
    rsp = json.loads(result)
    print result

    multi_sender = SmsSender.SmsMultiSender(appid, appkey)

    # 普通群发
    result = multi_sender.send(0, "86", phone_numbers, "测试短信，普通群发，深圳，小明，上学。", "", "")
    rsp = json.loads(result)
    print result

    # 指定模板群发
    # 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
    params = ["指定模板群发", "深圳", "小明"]
    result = multi_sender.send_with_param("86", phone_numbers, templ_id, params, "", "", "")
    rsp = json.loads(result)
    print result

def voice():
     # 语音验证码请求
     voice = VoiceSender(appid=133444444,appkey="2321312ada")
     result = voice.send(nation_code="86",phone_number="18678885558",playtimes=2,msg="1234",ext="")
     rsp = json.loads(result)
     if(int(rsp['result']) != 0):
         errmsg=rsp['errmsg']
         print "request failed\n"+errmsg
     else:
         print "request success\n"+result


    # 语音通知请求
     voice_promt = VoicePromptSender(appid=1123333, appkey="2321312ada")
     #note: msg内容，首先需要申请内容模板，通过后才可以发送
     result = voice_promt.send(nation_code="86", phone_number="156887998875", playtimes=2, msg="你好语音模板", ext="")
     rsp = json.loads(result)
     if (rsp['result'] != 0):
        errmsg = rsp['errmsg']
        print "request failed\n" + "error code: "+str(rsp['result'])+"\t"+errmsg
     else:
        print "request success\n" + result


if __name__ == "__main__":
    sms()
    voice()
