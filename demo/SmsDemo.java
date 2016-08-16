package com.tencent.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

// org.json 第三方库请自行下载编译，或者在以下链接下载使用 jdk 1.7 的版本
// http://share.weiyun.com/630a8c65e9fd497f3687b3546d0b839e
import org.json.JSONObject;

public class SmsDemo {
    public static void main(String[] args) {
        // 开放者实际发送短信时请使用申请的 sdkappid 和 appkey
        SmsSender sender = new SmsSender(1234567890, "1234567890");
        sender.sendMsg("86", "13012345678", "1234");
    }
}

class SmsSender {
    Random random = new Random();
    int sdkappid;
    String appkey;
    // 请根据我们的说明文档适时调整 url
    final String url = "https://yun.tim.qq.com/v3/tlssmssvr/sendsms";
    
    public SmsSender(int sdkappid, String appkey) {
        this.sdkappid = sdkappid;
        this.appkey = appkey;
    }
    
    public void sendMsg(String nationCode, String phoneNumber, String content) {
        long rnd = random.nextInt(999999)%(999999-100000+1)+100000;
        String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, sdkappid, rnd);        
        try {
            URL object = new URL(wholeUrl);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject data = new JSONObject();
            JSONObject tel = new JSONObject();
            tel.put("nationcode", nationCode);
            String phone = phoneNumber;
            tel.put("phone", phone);
            data.put("type", "0");
            data.put("msg", content);
            String sig = stringMD5(appkey.concat(phone));
            data.put("sig", sig);
            data.put("tel", tel);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "utf-8");
            System.out.println(data.toString());
            wr.write(data.toString());
            wr.flush();

            // 显示 POST 请求返回的内容
            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(con.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String stringMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] inputByteArray = input.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }
}
