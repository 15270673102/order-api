package com.loushi.utils.sts;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.loushi.component.properties.StsProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * sts
 * @author 技术部
 */

@Component
public class StsUtil {

    @Resource
    private StsProperties sts;

    /**
     * 获取token
     * @return
     * @throws ClientException
     */
    public HashMap<String, Object> getStsToken() throws Exception {
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", sts.getStsEndpoint());
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", sts.getAccessKeyId(), sts.getAccessKeySecret());
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(sts.getRoleArn());
            request.setRoleSessionName("java");
            AssumeRoleResponse response = client.getAcsResponse(request);

            HashMap<String, Object> map = new HashMap<>();
            map.put("Expiration", response.getCredentials().getExpiration());
            map.put("AccessKeyId", response.getCredentials().getAccessKeyId());
            map.put("AccessKeySecret", response.getCredentials().getAccessKeySecret());
            map.put("SecurityToken", response.getCredentials().getSecurityToken());
            map.put("bucketName", sts.getBucketName());
            map.put("RequestId", response.getRequestId());
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("阿里云sdk报错...");
        }

    }

}
