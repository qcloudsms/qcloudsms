# import httplib
import http.client

import json
import hashlib
import random
import time


class SmsSenderUtil:
    """ 工具类定义 """

    @staticmethod
    def get_random():
        return random.randint(100000, 999999)

    @staticmethod
    def get_cur_time():
        return int(time.time())

    @staticmethod
    def signature(appkey, rnd, timestamp, phone_number):
        s = 'appkey={}&random={}&time={}&mobile={}'.format(appkey, rnd, timestamp, phone_number)
        s = s.encode(encoding='utf-8')
        m = hashlib.sha256()
        m.update(s)
        c = m.hexdigest()
        return c

    @classmethod
    def calculate_signature(cls, appkey, rnd, cur_time, phone_numbers):
        phone_numbers_string = ','.join(phone_numbers)
        return cls.signature(appkey, rnd, cur_time, phone_numbers_string)

    @staticmethod
    def phone_numbers_to_list(nation_code, phone_numbers):
        tel = []
        for phone_number in phone_numbers:
            tel.append({"nationcode": nation_code, "mobile": phone_number})
        return tel

    @staticmethod
    def send_post_request(host, url, data):
        con = None
        try:
            con = http.client.HTTPSConnection(host)
            con.request('POST', url, json.dumps(data))
            response = con.getresponse()
            if '200' != str(response.status):
                obj = {}
                obj["result"] = -1
                obj["errmsg"] = "connect failed:\t" + str(response.status) + " " + response.reason
                result = json.dumps(obj)
            else:
                result = response.read().decode('utf-8')
        except Exception as e:
            obj = {}
            obj["result"] = -2
            obj["errmsg"] = "connect failed:\t" + str(e)
            result = json.dumps(obj)
        finally:
            if con:
                con.close()
        return result
