<?php

class SmsMultiSender {
    var $url;
    var $sdkappid;
    var $appkey;
    
    // sdkappid 使用整数即可
    function __construct($sdkappid, $appkey) {
        // url 需要根据我们说明文档上适时调整
        $this->url = "https://yun.tim.qq.com/v3/tlssmssvr/sendmultisms2";
        $this->sdkappid = $sdkappid;
        $this->appkey = $appkey;
    }
    
    // 全部参数使用字符串即可
    function sendSms($nationCode, $phoneNumbers, $content) {
        if (0 == count($phoneNumbers)) {
            return;
        }

        $randNum = rand(100000, 999999);
        $wholeUrl = $this->url."?sdkappid=".$this->sdkappid."&random=".$randNum;
        echo $wholeUrl."\n";

        $tel = array();
        for ($i = 0; $i < count($phoneNumbers); $i++) {
            $telElement = new stdClass();
            $telElement->nationcode = $nationCode;
            $telElement->phone = $phoneNumbers[$i];
            $tel[] = $telElement;
        }

        $jsondata = new stdClass();
        $jsondata->tel = $tel;
        $jsondata->type = "0";
        $jsondata->msg = $content;
        $jsondata->sig = $this->calculateSig($this->appkey, $phoneNumbers);
        $jsondata->extend = "";     // 根据需要添加，一般保持默认
        $jsondata->ext = "";        // 根据需要添加，一般保持默认
        $curlPost = json_encode($jsondata);
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

    function calculateSig($appkey, $phoneNumbers) {
        $cnt = count($phoneNumbers);
        $string = $appkey.$phoneNumbers[0];
        for ($i = 1; $i < $cnt; $i++) {
            $string = $string.",".$phoneNumbers[$i];
        }

        return md5($string);
    }
}

// 下面的 sdkappid 和 appkey 都是无法使用，开放者实际发送短信时请使用申请的 sdkappid 和 appkey
$sender = new SmsSender(1234567890, "1234567890");

// 下列手机号码均不存在，请替换成实际存在的
$phoneNumbers = array("12345678901", "12345678902", "12345678903");

// 请确保签名和模板审核通过
$sender->sendSms("86", $phoneNumbers, "验证码 1234");

?>
