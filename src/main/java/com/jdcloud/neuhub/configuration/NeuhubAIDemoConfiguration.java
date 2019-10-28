package com.jdcloud.neuhub.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestTemplate;

/**
 * 这是neuhub示例程序的配置类，这个类生成1个bean
 * <ul>
 * <li>1个{@link OAuth2RestTemplate}，这是请求时需要使用的rest客户端，spring提供了基于oauth2的实现，这样只需要通过配置就能自动完成token获取等操作</li>
 * <li>依赖的{@link OAuth2ProtectedResourceDetails}可由SpringBoot的auto configuration能力生成</li>
 * </ul>
 */
@Configuration
public class NeuhubAIDemoConfiguration {
    /**
     * 生成支撑OAuth2验证的rest客户端
     * @param resourceDetails - oauth2 客户端详情
     * @return - rest 客户端
     */
    @Bean
    public RestTemplate restTemplate(OAuth2ProtectedResourceDetails resourceDetails) {
        return new OAuth2RestTemplate(resourceDetails);
    }

}
