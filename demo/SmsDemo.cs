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
    class SmsSender
    {
        int sdkappid;
        string appkey;
        string url = "https://yun.tim.qq.com/v3/tlssmssvr/sendsms";

        public SmsSender(int sdkappid, string appkey)
        {
            this.sdkappid = sdkappid;
            this.appkey = appkey;
        }

        public void sendMsg(string nationCode, string phoneNumber, string content)
        {
            JObject data = new JObject();
            JObject tel = new JObject();
            tel.Add("nationcode", nationCode);
            tel.Add("phone", phoneNumber);
            data.Add("msg", content);
            string sig = stringMD5(appkey + phoneNumber);
            data.Add("type", "0");          // 默认为单发
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
            // sdkappid 和 appkey 请根据开放者申请的填写，下面的是无效的
            SmsSender sender = new SmsSender(1234567890, "1234567890");
            // 发送手机号以及发送内容请根据实际情况调整，特别是内容需要匹配模板
            sender.sendMsg("86", "13012345678", "验证码 1234");
        }
    }
}
