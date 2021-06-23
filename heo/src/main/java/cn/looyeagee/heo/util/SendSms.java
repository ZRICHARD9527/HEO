package cn.looyeagee.heo.util;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendSms {
    private static String accessKeyId;

    private static String accessKeySecret;

    @Value("${sms.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        SendSms.accessKeyId = accessKeyId;
    }

    @Value("${sms.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        SendSms.accessKeySecret = accessKeySecret;
    }

    public static void sendMsg(String phone,String code) {
        System.out.println(accessKeyId+accessKeySecret);
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "HEO校园互帮");
        request.putQueryParameter("TemplateCode", "SMS_192575909");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            client.getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
