package com.qcloud.sms;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

//org.json 第三方库请自行下载编译，或者在以下链接下载使用 jdk 1.7 的版本
//http://share.weiyun.com/630a8c65e9fd497f3687b3546d0b839e
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SmsVoiceUploader {
	String appkey;
	int appid;
	String url = "https://test.tim.qq.com/v3/tlsvoicesvr/upload_voice";
	//String url = "https://yun.tim.qq.com/v3/tlsvoicesvr/upload_voice";
	
	SmsSenderUtil util = new SmsSenderUtil();
	
	public SmsVoiceUploader(int appid, String appkey) {
		this.appid = appid;
		this.appkey = appkey;
	}
	
	/**
	 * 上传文件
	 * @param filePath 需要上传文件本地路径
	 * @return {@link}SmsVoiceUploaderResult
	 * @throws Exception
	 */	
	public SmsVoiceUploaderResult upload(String filePath) throws Exception {
		
		String wholeUrl = String.format("%s?sdkappid=%d", url, appid);
		String random = "" + util.getRandom();
		String curTime = "" + System.currentTimeMillis()/1000;
		
		File voiceFile = new File(filePath);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(wholeUrl);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();		

		ContentType contentType = ContentType.create("application/octet-stream;\r\nContent-Length: " + voiceFile.length());		
		builder.addBinaryBody("file", voiceFile, contentType, voiceFile.getName());
		builder.addTextBody("sig", util.strToHash(String.format("appkey=%s&rand=%s&time=%s", appkey, random, curTime)));
		builder.addTextBody("rand", random);
		builder.addTextBody("time", curTime);
		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		multipart.writeTo(bos);
		System.out.println(bos.toString());

		SmsVoiceUploaderResult result;
		int httpRspCode;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			httpRspCode = response.getStatusLine().getStatusCode();
			if (200 == httpRspCode) {
				BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				JSONObject json = new JSONObject(sb.toString());
				result = util.jsonToSmsVoiceUploaderResult(json);
				System.out.println(sb.toString());
			} else {
				result = new SmsVoiceUploaderResult();
				result.result = -1;
				result.msg = "http error " + httpRspCode;	
			}
		} finally {
			httpClient.close();
		}

		return result;
	}
}
