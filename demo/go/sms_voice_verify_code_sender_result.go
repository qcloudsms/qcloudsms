package qcloudsms

type SmsVoiceVerifyCodeSenderResult struct {
	Result int    `json:"result"`
	Errmsg string `json:"errmsg"`
	Ext    string `json:"ext"`
	Callid string `json:"callid"`
}
