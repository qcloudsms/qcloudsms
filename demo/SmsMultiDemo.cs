using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;
using System.Net;
using System.IO;

// newtonsoft json 模块请自行到 http://www.newtonsoft.com/json 下载
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace SMSDemo
{
    class SmsMultiSender
    {
        int sdkappid;
        string appkey;
        string url = "https://yun.tim.qq.com/v3/tlssmssvr/sendmultisms2";

        public SmsMultiSender(int sdkappid, string appkey)
        {
            this.sdkappid = sdkappid;
            this.appkey = appkey;
        }

        public void sendMsg(string nationCode, List<string> phoneNumbers, string content)
        {
            if (0 == phoneNumbers.Count)
            {
                return;
            }

            JObject data = new JObject();
            JArray tel = new JArray();
            for (int i = 0; i < phoneNumbers.Count; i++)
            {
                JObject telElement = new JObject();
                telElement.Add("nationcode", nationCode);
                telElement.Add("phone", phoneNumbers[i]);
                tel.Add(telElement);
            }
            data.Add("msg", content);
            data.Add("type", "0");          // 默认普通短信
            string sig = calculateSig(appkey, phoneNumbers);
            data.Add("sig", sig);
            data.Add("tel", tel);
            data.Add("extend", "");         // 根据需要添加，一般保持默认
            data.Add("ext", "");            // 根据需要添加，一般保持默认
            string msgString = JsonConvert.SerializeObject(data);
            Console.WriteLine(msgString);

            try
            {
                // 发送 POST 请求
                Random rnd = new Random();
                int random = rnd.Next(1000000) % (900000) + 1000000;
                string wholeUrl = url + "?sdkappid=" + sdkappid + "&random=" + random;
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(wholeUrl);
                request.Method = "POST";
                request.ContentType = "application/x-www-form-urlencoded";
                byte[] requestData = Encoding.UTF8.GetBytes(msgString);
                request.ContentLength = requestData.Length;
                Stream requestStream = request.GetRequestStream();
                requestStream.Write(requestData, 0, requestData.Length);
                requestStream.Close();

                // 接收返回包
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                Stream responseStream = response.GetResponseStream();
                StreamReader streamReader = new StreamReader(responseStream, Encoding.GetEncoding("utf-8"));
                string retString = streamReader.ReadToEnd();
                streamReader.Close();
                responseStream.Close();
                Console.WriteLine(retString);
                Console.ReadLine();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static string calculateSig(string appkey, List<string> phoneNumbers)
        {
            string str = appkey + phoneNumbers[0];
            for (int i = 1; i < phoneNumbers.Count; i++)
            {
                str = str + "," + phoneNumbers[i];
            }
            Console.WriteLine(str);
            return stringMD5(str);
        }

        private static string stringMD5(string input)
        {
            MD5 md5 = new MD5CryptoServiceProvider();
            byte[] targetData = md5.ComputeHash(System.Text.Encoding.UTF8.GetBytes(input));
            return byteToHexStr(targetData);
        }

        // 将二进制的数值转换为 16 进制字符串，如 "abc" => "616263"
        private static string byteToHexStr(byte[] input)
        {
            string returnStr = "";
            if (input != null)
            {
                for (int i = 0; i < input.Length; i++)
                {
                    returnStr += input[i].ToString("x2");
                }
            }
            return returnStr;
        }

        static void Main(string[] args)
        {
            // 下面的 sdkappid 和 appkey 都是无法使用，开放者实际发送短信时请使用申请的 sdkappid 和 appkey
            SmsMultiSender sender = new SmsMultiSender(1234567890, "1234567890");

            // 下列手机号码均不存在，请替换成实际存在的
            List<string> phoneNumbers = new List<string>();
            phoneNumbers.Add("12345678901");
            phoneNumbers.Add("12345678902");
            phoneNumbers.Add("12345678903");

            // 请确保签名和模板审核通过
            sender.sendMsg("86", phoneNumbers, "验证码 1234");
        }
    }
}
