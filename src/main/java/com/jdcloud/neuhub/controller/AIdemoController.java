package com.jdcloud.neuhub.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 humanDetect，该controller用于测试AI人脸识别接口，若要调用其他AI接口，请根据NeuhubAIDemo项目自行修改调用url和请求体
 NeuhubAIDemo项目地址：https://github.com/csd-sa/NeuhubAIDemo.git
 */
@RestController
@RequestMapping("/nanan")
public class AIdemoController {

    private Logger logger = LoggerFactory.getLogger(AIdemoController.class);

    private RestTemplate restTemplate;
    private ClientCredentialsResourceDetails clientCredentialsResourceDetails;
    /**
     * 调用的api网关地址
     */
    @Value("${gateway.url}")
    private String gatewayUrl;

    /**
     * 测试时使用的图片位置，在配置文件中进行修改
     */
    @Value("${neuhub.picture}")
    private String picture;

    /**
     * 人脸对比接口测试时使用的图片位置，在配置文件中进行修改
     */
    @Value("${neuhub.pictureCompare}")
    private String pictureCompare;

    /**
     * 测试时使用的文本内容，在配置文件中进行修改
     */
    @Value("${neuhub.comment}")
    private String comment;

    /**
     * 测试短文本相似度接口的文本内容，在配置文件中进行修改
     */
    @Value("${neuhub.commentCompare}")
    private String commentCompare;

    /**
     * 测试之前进行环境检查，确保输入正确的clientId和clientSecret
     */

    /**
     * 这不是一个普通的RestTemplate，而是引用的OAuth2RestTemplate
     *
     * @param restTemplate
     */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Autowired
    public void setClientCredentialsResourceDetails(ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
        this.clientCredentialsResourceDetails = clientCredentialsResourceDetails;
    }


    private void checkEnvironment() {
        int clientIdLength = 32;
        int clientSecretLength = 10;
        if (clientCredentialsResourceDetails.getClientId() == null || clientCredentialsResourceDetails.getClientId().length() != clientIdLength) {
            logger.error("clientId有误，请重新填写");
            System.exit(1);
        }

        if (clientCredentialsResourceDetails.getClientSecret() == null || clientCredentialsResourceDetails.getClientSecret().length() != clientSecretLength) {
            logger.error("clientSecret有误，请重新填写");
            System.exit(1);
        }
    }

    @GetMapping("/human_detect")
    public Object humanDetect() {
//        checkEnvironment();
        byte[] data = dataBinary(picture);
        HttpEntity<Object> requestEntity = new HttpEntity<>(data);
        //以下参数仅为示例值id
        String requestUrl = gatewayUrl + "/neuhub/human_detect";

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
        } catch (Exception e) {
            //调用API失败，错误处理
            throw new RuntimeException(e);
        }
        return responseEntity;
    }


    private byte[] dataBinary(String addr) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(addr));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
