package com.qcloud.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import org.json.JSONObject;

public class SmsStatusPuller {
	
	String url = "https://yun.tim.qq.com/v3/tlssmssvr/pullcallbackstatus";
	int appid;
	String appkey;
	
	SmsSenderUtil util = new SmsSenderUtil();
	
	public SmsStatusPuller(int appid, String appkey) {
		this.appid = appid;
		this.appkey = appkey;
	}
	
	/**
	 * 拉取回执统计结果
	 * @param beginDate 起始时间，格式例如 2016090800
	 * @param endDate 结束时间，格式例如  2016090823
	 * @return {@link}SmsStatusPullerResult
	 * @throws Exception
	 */
	public SmsStatusPullerResult pull(
			String beginDate, String endDate) throws Exception {
/*
{
	"sig": "xxxxxx", // sha256(appkey=$appkey&rand=$rand&time=$time)
	"begin_date": 2016090800, // yyyymmddhh需要拉取的起始时间,精确到小时
	"end_date": 2016090823, // yyyymmddhh需要拉取的截止时间,精确到小时
	"time": 1464624000 //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会拉取失败 
}
*/

		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis() / 1000;

		JSONObject data = new JSONObject();
		data.put("sig", util.strToHash(String.format(
				"appkey=%s&rand=%d&time=%d", appkey, random,curTime)));
		data.put("begin_date", beginDate);
		data.put("end_date", endDate);
		data.put("time", curTime);

		// 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
		HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
		wr.write(data.toString());
		wr.flush();

		// 显示 POST 请求返回的内容
		StringBuilder sb = new StringBuilder();
		int httpRspCode = conn.getResponseCode();
		SmsStatusPullerResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			result = util.jsonToSmsStatusPullerResult(json);
		} else {
			result = new SmsStatusPullerResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}

		return result;
	}
	
	

}
