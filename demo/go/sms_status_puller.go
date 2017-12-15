package qcloudsms

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

type SmsStatusPuller struct {
	appid  int
	appkey string
	url    string
}

func NewSmsStatusPuller(appid int, appkey string) *SmsStatusPuller {
	return &SmsStatusPuller{
		appid:  appid,
		appkey: appkey,
		url:    "https://yun.tim.qq.com/v5/tlssmssvr/pullstatus",
	}
}

/**
 * 拉取回执结果
 * max 最大条数 最多100
 */
func (ss *SmsStatusPuller) PullCallback(max int) (*SmsStatusPullCallbackResult, error) {
	/*
		{
			"sig": "xxxxxx", // sha256(appkey=$appkey&rand=$rand&time=$time)
			"type": 0, // 类型
			"max": 10, //最大条数
			"time": 1464624000 //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会拉取失败
		}
	*/
	// 按照协议组织 post 请求包体
	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["sig"] = strToHash(fmt.Sprintf("appkey=%s&random=%d&time=%d", ss.appkey, random, curTime))
	data["time"] = curTime
	data["type"] = 0 //0表示短信回执
	data["max"] = max
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", ss.url, ss.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsStatusPullCallbackResult{}
	err = apiRequest(req, result)
	return result, err
}

/**
 * 拉取回复信息
 * max 最大条数 最多100
 */
func (ss *SmsStatusPuller) PullReply(max int) (*SmsStatusPullReplyResult, error) {
	/*
		{
			"sig": "xxxxxx", // sha256(appkey=$appkey&rand=$rand&time=$time)
			"type": 1, // 类型
			"max": 10, //最大条数
			"time": 1464624000 //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会拉取失败
		}
	*/
	// 按照协议组织 post 请求包体
	random := getRandom()
	curTime := getTime()
	data := make(map[string]interface{})
	data["sig"] = strToHash(fmt.Sprintf("appkey=%s&random=%d&time=%d", ss.appkey, random, curTime))
	data["time"] = curTime
	data["type"] = 1 //1表示回复
	data["max"] = max
	wholeUrl := fmt.Sprintf("%s?sdkappid=%d&random=%d", ss.url, ss.appid, random)
	body, _ := json.Marshal(data)
	req, err := http.NewRequest("POST", wholeUrl, bytes.NewReader(body))
	if err != nil {
		return nil, fmt.Errorf("make http req error %v", err)
	}
	result := &SmsStatusPullReplyResult{}
	err = apiRequest(req, result)
	return result, err
}
