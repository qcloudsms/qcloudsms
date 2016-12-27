import java.util.ArrayList;

import com.qcloud.sms.SmsMultiSender;
import com.qcloud.sms.SmsMultiSenderResult;
import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;

public class SmsSDKDemo {
    public static void main(String[] args) {
    	try {
    		// 请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
    		int appid = 123456;
    		String appkey = "1234567890abcdef1234567890abcdef";
    		
    		String phoneNumber1 = "12345678901";
    		String phoneNumber2 = "12345678902";
    		String phoneNumber3 = "12345678903";
    		int tmplId = 7839;

    		// 初始化单发
	    	SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
	    	SmsSingleSenderResult singleSenderResult;
	
	    	// 普通单发
	    	singleSenderResult = singleSender.send(0, "86", phoneNumber2, "测试短信，普通单发，深圳，小明，上学。", "", "");
	    	System.out.println(singleSenderResult);
	
	    	// 指定模板单发
	    	// 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
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
	    	// 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
	    	params = new ArrayList<>();
	    	params.add("指定模板群发");
	    	params.add("深圳");
	    	params.add("小明");
	    	multiSenderResult = multiSender.sendWithParam("86", phoneNumbers, tmplId, params, "", "", "");
	    	System.out.println(multiSenderResult);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
