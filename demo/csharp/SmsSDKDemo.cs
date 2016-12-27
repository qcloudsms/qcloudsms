using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

using Qcloud.Sms;

namespace SmsSDK
{
    class Demo
    {
        public int result { get; set; }
        public string errMsg { get; set; }
        public string ext { get; set; }
    }

    class SmsSDKDemo
    {
        static void Main(string[] args)
        {
            // 请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
            int sdkappid = 123456;
            string appkey = "1234567890abcdef1234567890abcdef";
            string phoneNumber1 = "12345678901";
            string phoneNumber2 = "12345678902";
            string phoneNumber3 = "12345678903";
            int tmplId = 7839;

            try
            {
                SmsSingleSenderResult singleResult;
                SmsSingleSender singleSender = new SmsSingleSender(sdkappid, appkey);

                singleResult = singleSender.Send(0, "86", phoneNumber2, "测试短信，普通单发，深圳，小明，上学。", "", "");
                Console.WriteLine(singleResult);

                List<string> templParams = new List<string>();
                templParams.Add("指定模板单发");
                templParams.Add("深圳");
                templParams.Add("小明");
                // 指定模板单发
                // 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
                singleResult = singleSender.SendWithParam("86", phoneNumber2, tmplId, templParams, "", "", "");
                Console.WriteLine(singleResult);

                SmsMultiSenderResult multiResult;
                SmsMultiSender multiSender = new SmsMultiSender(sdkappid, appkey);
                List<string> phoneNumbers = new List<string>();
                phoneNumbers.Add(phoneNumber1);
                phoneNumbers.Add(phoneNumber2);
                phoneNumbers.Add(phoneNumber3);

                // 普通群发
                // 下面是 3 个假设的号码
                multiResult = multiSender.Send(0, "86", phoneNumbers, "测试短信，普通群发，深圳，小明，上学。", "", "");
                Console.WriteLine(multiResult);

                // 指定模板群发
                // 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
                templParams.Clear();
                templParams.Add("指定模板群发");
                templParams.Add("深圳");
                templParams.Add("小明");
                multiResult = multiSender.SendWithParam("86", phoneNumbers, tmplId, templParams, "", "", "");
                Console.WriteLine(multiResult);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
    }
}
