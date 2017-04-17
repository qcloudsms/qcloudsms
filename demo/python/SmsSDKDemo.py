#! /usr/bin/env python2
# encoding:utf-8

import json
import Qcloud.Sms.SmsSender as SmsSender


def main():
    # 请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
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

if __name__ == "__main__":
    main()
