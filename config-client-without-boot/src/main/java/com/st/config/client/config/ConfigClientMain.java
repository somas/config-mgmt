package com.st.config.client.config;

import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.st.config.client.service.PropertiesCtrl;
import com.st.config.client.service.impl.GlobalScopedParams;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.ribbon.LoadBalancingTarget;
import org.springframework.context.annotation.*;

@Configuration
@EnableMBeanExport
@ComponentScan("com.st.config.client")
public class ConfigClientMain {

    public static void main(String[] args) throws Exception {
        GlobalScopedParams globalScopedParams = getGlobalScopedParams();
        System.out.println("EXPECTED: 'y' : GOT : " + globalScopedParams.get("amazon", "integration", "x", "default"));
        System.out.println("EXPECTED: 'default' : GOT : " + globalScopedParams.get("amazonian", "integration", "x", "default"));
    }

    public static GlobalScopedParams getGlobalScopedParams() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ConfigClientMain.class);
        ctx.refresh();
        return ctx.getBean(GlobalScopedParams.class);
    }

    @Bean
    public PropertiesCtrl propertiesCtrl() {
        DiscoveryManager.getInstance().initComponent(
                new MyDataCenterInstanceConfig(),
                new DefaultEurekaClientConfig());

        /**
         IRule rule = new AvailabilityFilteringRule();
         ServerList<DiscoveryEnabledServer> list = new DiscoveryEnabledNIWSServerList("CONFIG-SERVER");
         ServerListFilter<DiscoveryEnabledServer> filter = new ZoneAffinityServerListFilter<>();

         ZoneAwareLoadBalancer<DiscoveryEnabledServer> lb = LoadBalancerBuilder.<DiscoveryEnabledServer>newBuilder()
         .withDynamicServerList(list)
         .withRule(rule)
         .withServerListFilter(filter)
         .buildDynamicServerListLoadBalancer();

         PropertiesCtrl propertiesCtrl = Feign.builder().encoder(new JacksonEncoder())
         .decoder(new JacksonDecoder()).requestInterceptor(new BasicAuthRequestInterceptor("somas", "password"))
         .target(MyLoadBalancingTarget.create(PropertiesCtrl.class, "http://client-config", lb));

         */

        return Feign.builder().encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder()).requestInterceptor(new BasicAuthRequestInterceptor("somas", "password"))
                .target(LoadBalancingTarget.create(PropertiesCtrl.class, "http://client"));
    }
}