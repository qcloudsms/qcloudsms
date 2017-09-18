import java.util.ArrayList;

import com.qcloud.sms.SmsMultiSender;
import com.qcloud.sms.SmsMultiSenderResult;
import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;
import com.qcloud.sms.SmsStatusPuller;
import com.qcloud.sms.SmsStatusPullCallbackResult;
import com.qcloud.sms.SmsStatusPullReplyResult;
import com.qcloud.sms.SmsVoiceVerifyCodeSender;
import com.qcloud.sms.SmsVoiceVerifyCodeSenderResult;
import com.qcloud.sms.SmsVoicePromptSender;
import com.qcloud.sms.SmsVoicePromptSenderResult;

public class SmsSDKDemo {
    public static void main(String[] args) {
    	try {
    		//请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
			//appid,appkey,templId申请方式可参考接入指南 https://www.qcloud.com/document/product/382/3785#5-.E7.9F.AD.E4.BF.A1.E5.86.85.E5.AE.B9.E9.85.8D.E7.BD.AE
    		int appid = 123345555;
    		String appkey = "1123323412asdadaf323fdadssa";
    		
    		String phoneNumber1 = "12345678902";
    		String phoneNumber2 = "12345678902";
    		String phoneNumber3 = "12345678903";
    		int tmplId = 7839;

    		 //初始化单发
	    	SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
	    	SmsSingleSenderResult singleSenderResult;
	
	    	 //普通单发
	    	singleSenderResult = singleSender.send(0, "86", phoneNumber1, "您注册的验证码：1234", "", "");
	    	System.out.println(singleSenderResult);
	
	    	 //指定模板单发
	    	 //假设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
	    	ArrayList<String> params = new ArrayList<>();
	    	params.add("指定模板单发");
	    	params.add("深圳");
	    	params.add("小明");
	    	singleSenderResult = singleSender.sendWithParam("86", phoneNumber2, tmplId, params, "", "", "");
	    	System.out.println(singleSenderResult);
	    	
	    	// 初始化群发
	    	SmsMultiSender multiSender = new SmsMultiSender(appid, appkey);
	    	SmsMultiSenderResult multiSenderResult;
	
	    	// 普通群发
	    	// 下面是 3 个假设的号码
	    	ArrayList<String> phoneNumbers = new ArrayList<>();
	    	phoneNumbers.add(phoneNumber1);
	    	phoneNumbers.add(phoneNumber2);
	    	phoneNumbers.add(phoneNumber3);
	    	multiSenderResult = multiSender.send(0, "86", phoneNumbers, "测试短信，普通群发，深圳，小明，上学。", "", "");
	    	System.out.println(multiSenderResult);

	    	// 指定模板群发
	    	// 假设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
	    	params = new ArrayList<>();
	    	params.add("指定模板群发");
	    	params.add("深圳");
	    	params.add("小明");
	    	multiSenderResult = multiSender.sendWithParam("86", phoneNumbers, tmplId, params, "", "", "");
	    	System.out.println(multiSenderResult);
	    	
    		
    		//拉取短信回执和回复
    		SmsStatusPuller pullstatus = new SmsStatusPuller(appid, appkey);
    		SmsStatusPullCallbackResult callbackResult = pullstatus.pullCallback(10);
    		System.out.println(callbackResult);
    		SmsStatusPullReplyResult replyResult = pullstatus.pullReply(10);
    		System.out.println(replyResult);
			
			// 发送通知内容
    		SmsVoicePromptSender smsVoicePromtSender = new SmsVoicePromptSender(appid, appkey);
    		SmsVoicePromptSenderResult smsSingleVoiceSenderResult = smsVoicePromtSender.send("86", phoneNumber1, 2,2,"欢迎使用", "");
	    	System.out.println(smsSingleVoiceSenderResult);

    		//语音验证码发送
    		SmsVoiceVerifyCodeSender smsVoiceVerifyCodeSender = new SmsVoiceVerifyCodeSender(appid,appkey);
    		SmsVoiceVerifyCodeSenderResult smsVoiceVerifyCodeSenderResult = smsVoiceVerifyCodeSender.send("86",phoneNumber1, "123",2,"");
    		System.out.println(smsVoiceVerifyCodeSenderResult);
			
    		
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
