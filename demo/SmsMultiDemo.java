package com.tencent.demo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class SmsMultiDemo {
    public static void main(String[] args) {
        // 下面的 sdkappid 和 appkey 都是无法使用，开放者实际发送短信时请使用申请的 sdkappid 和 appkey
        SmsMultiSender sender = new SmsMultiSender(1234567890, "1234567890abcdef1234567890abcdef");

        ArrayList<String> phoneNumbers = new ArrayList<>();
        // 下列手机号码均不存在，请替换成实际存在的
        phoneNumbers.add("12345678901");
        phoneNumbers.add("12345678902");
        phoneNumbers.add("12345678903");

        // 请确保签名和模板审核通过
        sender.sendMsg("86", phoneNumbers, "验证码 1234");
    }
}

class SmsMultiSender {
    Random random = new Random();
    int sdkappid;
    String appkey;
    // 请根据我们的说明文档适时调整 url
    final String url = "https://yun.tim.qq.com/v3/tlssmssvr/sendmultisms2";

    public SmsMultiSender(int sdkappid, String appkey) {
        this.sdkappid = sdkappid;
        this.appkey = appkey;
    }

    public void sendMsg(String nationCode, ArrayList<String> phoneNumbers, String content) {
        if (0 == phoneNumbers.size()) {
            return;
        }

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

            JSONArray tel = new JSONArray();

            int i = 0;
            do {
                JSONObject telElement = new JSONObject();
                telElement.put("nationcode", nationCode);
                telElement.put("phone", phoneNumbers.get(i));
                tel.put(telElement);
            } while (++i < phoneNumbers.size());

            data.put("tel", tel);
            data.put("type", "0");
            data.put("msg", content);
            String sig = calculateSig(appkey, phoneNumbers);
            data.put("sig", sig);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "utf-8");
            wr.write(data.toString());
            wr.flush();

            System.out.println(data.toString());

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

    private static String calculateSig(String appkey, ArrayList<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return stringMD5(appkey.concat(phoneNumbersString));
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
