package qcloudsms

type SmsMultiSenderResult struct {
	Detail []struct {
		Result     int    `json:"result"`
		ErrMsg     string `json:"errmsg"`
		Mobile     string `json:"moblie"`
		NationCode string `json:"nationcode"`
		Sid        string `json:"sid"`
		Fee        int    `json:"fee"`
	} `json:"detail"`
	Result int    `json:"result"`
	ErrMsg string `json:"errmsg"`
	Ext    string `json:"ext"`
}
