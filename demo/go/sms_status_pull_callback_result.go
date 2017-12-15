package qcloudsms

type SmsStatusPullCallbackResult struct {
	Result    int    `json:"int"`
	Errmsg    string `json:"errmsg"`
	Count     int    `json:"count"`
	Callbacks []struct {
		UserTeceiveTime string `json:"user_receive_time"`
		Nationcode      string `json:"nationcode"`
		Mobile          string `json:"mobile"`
		ReportStatus    string `json:"report_status"`
		Errmsg          string `json:"errmsg"`
		Description     string `json:"description"`
		Sid             string `json:"sid"`
	} `json:"data"`
}
