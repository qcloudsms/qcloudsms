package qcloudsms

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

type SmsVoiceVerifyCodeSender struct {
	appid  int
	appkey string
	url    string
}

func NewSmsVoiceVerifyCodeSender(appid int, appkey string) *SmsVoiceVerifyCodeSender {
	return &SmsVoiceVerifyCodeSender{
		appid:  appid,
		appkey: appkey,
		url:    "https://yun.tim.qq.com/v5/tlsvoicesvr/sendvoice",
	}
}

/*
 * 发送语音短信
 * nationCode 国家码，如 86 为中国
 * phoneNumber 不带国家码的手机号
 * msg 消息类型
 * playtimes 播放次数
 * ext 服务端原样返回的参数，可填空
 */
func (sv *SmsVoiceVerifyCodeSender) Send(
	nationCode string,
	phoneNumber string,
	msg string,
	playtimes int,
	ext string) (*SmsVoiceVerifyCodeSenderResult, error) {

	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["msg"] = msg
	data["playtimes"] = playtimes
	data["sig"] = calculateSig(sv.appkey, random, curTime, []string{phoneNumber})
	data["tel"] = map[string]string{
		"nationcode": nationCode,
		"mobile":     phoneNumber,
	}
	data["time"] = curTime
	data["ext"] = ext
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", sv.url, sv.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsVoiceVerifyCodeSenderResult{}
	err = apiRequest(req, result)
	return result, err
}
