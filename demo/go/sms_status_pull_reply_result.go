package qcloudsms

type SmsStatusPullReplyResult struct {
	Result int    `json:"int"`
	Errmsg string `json:"errmsg"`
	Count  int    `json:"count"`
	Replys []struct {
		Nationcode string `json:"nationcode"`
		Mobile     string `json:"mobile"`
		Text       string `json:"text"`
		Sign       string `json:"sign"`
		Time       int64  `json:"time"`
		Extend     string `json:"extend"`
	} `json:"data"`
}
