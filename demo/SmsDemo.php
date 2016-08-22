<?php

class SmsSender {
    var $url;
    var $sdkappid;
    var $appkey;
    
    // sdkappid 使用整数即可
    function __construct($sdkappid, $appkey) {
        // url 需要根据我们说明文档上适时调整
        $this->url = "https://yun.tim.qq.com/v3/tlssmssvr/sendsms";
        $this->sdkappid = $sdkappid;
        $this->appkey = $appkey;
    }
    
    // 全部参数使用字符串即可
    function sendSms($nationCode, $phoneNumber, $content) {        
        $randNum = rand(100000, 999999);
        $wholeUrl = $this->url."?sdkappid=".$this->sdkappid."&random=".$randNum;
        echo $wholeUrl;
        $tel = new stdClass();
        $tel->nationcode = $nationCode;
        $tel->phone = $phoneNumber;
        $jsondata = new stdClass();
        $jsondata->tel = $tel;
        $jsondata->type = "0";
        $jsondata->msg = $content;
        $jsondata->sig = md5($this->appkey.$phoneNumber);
        $jsondata->extend = "";     // 根据需要添加，一般保持默认
        $jsondata->ext = "";        // 根据需要添加，一般保持默认
        $curlPost =json_encode($jsondata);
        echo $curlPost;
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $wholeUrl);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $curlPost);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        $ret = curl_exec($ch);

        if($ret === false) {
            var_dump(curl_error($ch));
        } else {
            $json = json_decode($ret);
            if($json === false) {
                var_dump($ret);
            } else {
                var_dump($json);
            }
        }
        curl_close($ch);
        return;
    }
}

$sender = new SmsSender(1234567890, "1234567890");
$sender->sendSms("86", "13012345678", "验证码 1234");
?>
