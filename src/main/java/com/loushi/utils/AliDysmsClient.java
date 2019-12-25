package com.loushi.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.loushi.component.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 阿里云发短信
 */

@Slf4j
@Component
public class AliDysmsClient {

    @Resource
    private SmsProperties smsProperties;


    /**
     * 注册发送验证码
     * @param phoneNum
     * @param code
     */
    public boolean registerSendSms(String phoneNum, String code) throws Exception {
        String signName = "注册验证";
        String templateCode = "SMS_143862154";
        String templateParams = "{\"code\":\"" + code + "\"}";
        String outId = "";
        SendSmsResponse res = sendSms(phoneNum, signName, templateCode, templateParams, outId);
        log.info("短信接口返回的数据... code:{}, message:{}, requestId:{}, bizId:{}", res.getCode(), res.getMessage(), res.getRequestId(), res.getBizId());
        return "OK".equals(res.getCode());
    }

    /**
     * 发短信
     * @param phone
     * @param signName
     * @param templateCode
     * @param templateParams
     * @param outId
     */
    private SendSmsResponse sendSms(String phone, String signName, String templateCode, String templateParams, String outId) throws Exception {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        try {
            // 初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", smsProperties.getProduct(), smsProperties.getDomain());
            IAcsClient acsClient = new DefaultAcsClient(profile);

            // 组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            // 必填:待发送手机号
            request.setPhoneNumbers(phone);
            // 必填:短信签名-可在短信控制台中找到
            // request.setSignName("活动验证");
            request.setSignName(signName);
            // 必填:短信模板-可在短信控制台中找到
            // request.setTemplateCode("SMS_62505324");
            request.setTemplateCode(templateCode);
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            // request.setTemplateParam("{\"product\":\"妞妞端午\", \"code\":\"123\",\"item\":\"公仔兑换\"}");
            request.setTemplateParam(templateParams);

            // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            // request.setSmsUpExtendCode("90997");

            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId(outId);
            return acsClient.getAcsResponse(request);

        } catch (Exception e) {
            throw new Exception("验证码发送失败...");
        }
    }

}
