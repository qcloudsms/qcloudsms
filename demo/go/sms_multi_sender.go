package qcloudsms

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

type SmsMultiSender struct {
	appid  int
	appkey string
	url    string
}

func NewSmsMultiSender(appid int, appkey string) *SmsMultiSender {
	return &SmsMultiSender{
		appid:  appid,
		appkey: appkey,
		url:    "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2",
	}
}

/*
 * 普通群发，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
 * 【注意】海外短信无群发功能
 * type 短信类型，0 为普通短信，1 营销短信
 * nationCode 国家码，如 86 为中国
 * phoneNumbers 不带国家码的手机号列表
 * msg 信息内容，必须与申请的模板格式一致，否则将返回错误
 * extend 扩展码，可填空
 * ext 服务端原样返回的参数，可填空
 */
func (sm *SmsMultiSender) Send(
	typ int,
	nationCode string,
	phoneNumbers []string,
	msg string,
	extend string,
	ext string) (*SmsMultiSenderResult, error) {
	/*
	   请求包体
	   {
	       "tel": [
	           {
	               "nationcode": "86",
	               "mobile": "13788888888"
	           },
	           {
	               "nationcode": "86",
	               "mobile": "13788888889"
	           }
	       ],
	       "type": 0,
	       "msg": "你的验证码是1234",
	       "sig": "fdba654e05bc0d15796713a1a1a2318c",
	       "time": 1479888540,
	       "extend": "",
	       "ext": ""
	   }
	   应答包体
	   {
	       "result": 0,
	       "errmsg": "OK",
	       "ext": "",
	       "detail": [
	           {
	               "result": 0,
	               "errmsg": "OK",
	               "mobile": "13788888888",
	               "nationcode": "86",
	               "sid": "xxxxxxx",
	               "fee": 1
	           },
	           {
	               "result": 0,
	               "errmsg": "OK",
	               "mobile": "13788888889",
	               "nationcode": "86",
	               "sid": "xxxxxxx",
	               "fee": 1
	           }
	       ]
	   }
	*/
	if typ != 0 && typ != 1 {
		return nil, fmt.Errorf("type %d error", typ)
	}
	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["type"] = typ
	data["msg"] = msg
	data["sig"] = calculateSig(sm.appkey, random, curTime, phoneNumbers)
	tel := make([]map[string]string, 0, len(phoneNumbers))
	for _, phoneNumber := range phoneNumbers {
		tel = append(tel, map[string]string{
			"nationcode": nationCode,
			"mobile":     phoneNumber,
		})
	}
	data["tel"] = tel
	data["time"] = curTime
	data["extend"] = extend
	data["ext"] = ext
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", sm.url, sm.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsMultiSenderResult{}
	err = apiRequest(req, result)
	return result, err
}

/*
 * 指定模板群发
 * 【注意】海外短信无群发功能
 *  nationCode 国家码，如 86 为中国
 *  phoneNumbers 不带国家码的手机号列表
 *  templId 模板 id
 *  params 模板参数列表
 *  sign 签名，如果填空，系统会使用默认签名
 *  extend 扩展码，可以填空
 *  ext 服务端原样返回的参数，可以填空
 */
func (sm *SmsMultiSender) SendWithParam(
	nationCode string,
	phoneNumbers []string,
	templId int,
	params []string,
	sign string,
	extend string,
	ext string) (*SmsMultiSenderResult, error) {
	/*
	   请求包体
	   {
	       "tel": [
	           {
	               "nationcode": "86",
	               "mobile": "13788888888"
	           },
	           {
	               "nationcode": "86",
	               "mobile": "13788888889"
	           }
	       ],
	       "sign": "腾讯云",
	       "tpl_id": 19,
	       "params": [
	           "验证码",
	           "1234",
	           "4"
	       ],
	       "sig": "fdba654e05bc0d15796713a1a1a2318c",
	       "time": 1479888540,
	       "extend": "",
	       "ext": ""
	   }
	   应答包体
	   {
	       "result": 0,
	       "errmsg": "OK",
	       "ext": "",
	       "detail": [
	           {
	               "result": 0,
	               "errmsg": "OK",
	               "mobile": "13788888888",
	               "nationcode": "86",
	               "sid": "xxxxxxx",
	               "fee": 1
	           },
	           {
	               "result": 0,
	               "errmsg": "OK",
	               "mobile": "13788888889",
	               "nationcode": "86",
	               "sid": "xxxxxxx",
	               "fee": 1
	           }
	       ]
	   }
	*/
	if nationCode == "" {
		nationCode = "86"
	}
	if len(phoneNumbers) == 0 {
		return nil, fmt.Errorf("phoneNumbers size error")
	}
	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["sig"] = calculateSig(sm.appkey, random, curTime, phoneNumbers)
	tel := make([]map[string]string, 0, len(phoneNumbers))
	for _, phoneNumber := range phoneNumbers {
		tel = append(tel, map[string]string{
			"nationcode": nationCode,
			"mobile":     phoneNumber,
		})
	}
	data["tel"] = tel
	data["sign"] = sign
	data["tpl_id"] = templId
	data["params"] = params
	data["time"] = curTime
	data["extend"] = extend
	data["ext"] = ext
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", sm.url, sm.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsMultiSenderResult{}
	err = apiRequest(req, result)
	return result, err
}
