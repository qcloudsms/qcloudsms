package qcloudsms

type SmsVoicePromptSenderResult struct {
	Result int    `json:"result"`
	Errmsg string `json:"errmsg"`
	Ext    string `json:"ext"`
	Callid string `json:"callid"`
}
