package qcloudsms

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

// SmsSingleSender实现单发短信
type SmsSingleSender struct {
	appid  int
	appkey string
	url    string
}

func NewSmsSingleSender(appid int, appkey string) *SmsSingleSender {
	return &SmsSingleSender{
		appid:  appid,
		appkey: appkey,
		url:    "https://yun.tim.qq.com/v5/tlssmssvr/sendsms",
	}
}

/*
 * 普通单发短信接口，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
 * typ 短信类型，0 为普通短信，1 营销短信
 * nationCode 国家码，如 86 为中国
 * phoneNumber 不带国家码的手机号
 * msg 信息内容，必须与申请的模板格式一致，否则将返回错误
 * extend 扩展码，可填空
 * ext 服务端原样返回的参数，可填空
 */
func (ss *SmsSingleSender) Send(
	typ int,
	nationCode string,
	phoneNumber string,
	msg string,
	extend string,
	ext string) (*SmsSingleSenderResult, error) {
	/*
	   请求包体
	   {
	       "tel": {
	           "nationcode": "86",
	           "mobile": "13788888888"

	   },
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
	       "sid": "xxxxxxx",
	       "fee": 1

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
	data["sig"] = calculateSig(ss.appkey, random, curTime, []string{phoneNumber})
	data["tel"] = map[string]string{
		"nationcode": nationCode,
		"mobile":     phoneNumber,
	}
	data["time"] = curTime
	data["extend"] = extend
	data["ext"] = ext
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", ss.url, ss.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsSingleSenderResult{}
	err = apiRequest(req, result)
	return result, err
}

/*
 * 指定模板单发
 * nationCode 国家码，如 86 为中国
 * phoneNumber 不带国家码的手机号
 * templId 信息内容
 * params 模板参数列表，如模板 {1}...{2}...{3}，那么需要带三个参数
 * sign 签名，如果填空，系统会使用默认签名
 * extend 扩展码，可填空
 * ext 服务端原样返回的参数，可填空
 */
func (ss *SmsSingleSender) SendWithParam(
	typ int,
	nationCode string,
	phoneNumber string,
	templId int,
	params []string,
	sign string,
	extend string,
	ext string) (*SmsSingleSenderResult, error) {
	/*
		请求包体
		{
		    "tel": {
		        "nationcode": "86",
		        "mobile": "13788888888"
		    },
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
		    "sid": "xxxxxxx",
		    "fee": 1
		}
	*/
	if typ != 0 && typ != 1 {
		return nil, fmt.Errorf("type %d error", typ)
	}
	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["type"] = typ
	data["sig"] = calculateSig(ss.appkey, random, curTime, []string{phoneNumber})
	data["tpl_id"] = templId
	data["params"] = params
	data["tel"] = map[string]string{
		"nationcode": nationCode,
		"mobile":     phoneNumber,
	}
	data["sign"] = sign
	data["time"] = curTime
	data["extend"] = extend
	data["ext"] = ext
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", ss.url, ss.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsSingleSenderResult{}
	err = apiRequest(req, result)
	return result, err
}
