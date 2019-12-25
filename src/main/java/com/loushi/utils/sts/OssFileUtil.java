package com.loushi.utils.sts;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.loushi.component.properties.StsProperties;
import com.loushi.util.CalendarUtil;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * @author 技术部
 */

@Component
public class OssFileUtil {

    @Resource
    private StsProperties sts;

    /**
     * 创建OSSClient实例
     */
    private OSSClient initOssClient() {
        return new OSSClient(sts.getEndpoint(), sts.getAccessKeyId(), sts.getAccessKeySecret());
    }

    /**
     * 根据前端的图片名字，获取OSS文件的http地址
     * @param fileName
     * @return
     */
    public String getOssFilePath(String fileName) {
        OSSClient ossClient = initOssClient();
        Date expiration = CalendarUtil.hourNumber(new Date(), 1);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(sts.getBucketName(), fileName, HttpMethod.GET);
        // 设置过期时间。
        request.setExpiration(expiration);
        // 生成签名URL（HTTP GET请求）。
        String filePath = ossClient.generatePresignedUrl(request).toString();
        ossClient.shutdown();
        return filePath;
    }


    /**
     * 通过网络连接上传文件
     * @param fileName
     * @param url
     * @return
     * @throws IOException
     */
    public void uploadFileObjectByStream(String fileName, String url) throws IOException {
        OSSClient ossClient = initOssClient();
        ossClient.putObject(sts.getBucketName(), fileName, new URL(url).openStream());
        ossClient.shutdown();
    }

    /**
     * 通过inputStream上传文件
     * @param fileName
     * @param inputStream
     */
    public void uploadFileObjectByStream(String fileName, InputStream inputStream) {
        OSSClient ossClient = initOssClient();
        ossClient.putObject(sts.getBucketName(), fileName, inputStream);
        ossClient.shutdown();
    }

    /**
     * 通过文件路径 上传文件
     * @param fileName
     * @param filePath
     */
    public void uploadFileObjectByFile(String fileName, String filePath) {
        OSSClient ossClient = initOssClient();
        ossClient.putObject(sts.getBucketName(), fileName, new File(filePath));
        ossClient.shutdown();
    }

    /**
     * base64转InputStream
     * @param base64string
     * @return
     * @throws IOException
     */
    public InputStream BaseToInputStream(String base64string) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes1 = decoder.decodeBuffer(base64string.trim());
        return new ByteArrayInputStream(bytes1);
    }


    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgFile
     * @return
     */
    public String imgFileToBase64(String imgFile) throws IOException {
        // 读取图片字节数组
        InputStream in = new FileInputStream(imgFile);
        byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

}
