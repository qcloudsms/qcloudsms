package qcloudsms

/*
{
    "result": 0,
    "errmsg": "OK",
    "ext": "",
    "sid": "xxxxxxx",
    "fee": 1

}
*/
type SmsSingleSenderResult struct {
	Result int    `json:"result"`
	ErrMsg string `json:"errmsg"`
	Ext    string `json:"ext"`
	Sid    string `json:"sid"`
	Fee    int    `json:"fee"`
}
