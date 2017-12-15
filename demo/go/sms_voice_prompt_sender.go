package qcloudsms

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

type SmsVoicePromptSender struct {
	appid  int
	appkey string
	url    string
}

func NewSmsVoicePromptSender(appid int, appkey string) *SmsVoicePromptSender {
	return &SmsVoicePromptSender{
		appid:  appid,
		appkey: appkey,
		url:    "https://yun.tim.qq.com/v5/tlsvoicesvr/sendvoiceprompt",
	}
}

/**
 * 发送语音短信
 * nationCode 国家码，如 86 为中国
 * phoneNumber 不带国家码的手机号
 * prompttype 类型，目前固定值为2
 * playtimes 播放次数
 * msg 语音通知消息内容
 * ext  "扩展字段，原样返回"
 */
func (sv *SmsVoicePromptSender) Send(
	nationCode string,
	phoneNumber string,
	promptType int,
	playTimes int,
	msg string,
	ext string) (*SmsVoicePromptSenderResult, error) {

	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["playtimes"] = playTimes
	data["prompttype"] = promptType
	data["promptfile"] = promptType
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
	result := &SmsVoicePromptSenderResult{}
	err = apiRequest(req, result)
	return result, err
}
