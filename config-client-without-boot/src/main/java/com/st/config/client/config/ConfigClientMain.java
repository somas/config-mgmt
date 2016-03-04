package com.st.config.client.config;

import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.client.ClientFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.st.config.client.bean.Properties;
import com.st.config.client.service.PropertiesCtrl;
import feign.Feign;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.ribbon.LoadBalancingTarget;
import org.springframework.context.annotation.*;

import java.net.URI;

import static feign.Util.checkNotNull;
import static java.lang.String.format;

@Configuration
@EnableMBeanExport
@ComponentScan("com.st.config.client")
public class ConfigClientMain {

    public static void main(String[] args) throws Exception {


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

        PropertiesCtrl propertiesCtrlB = Feign.builder().encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder()).requestInterceptor(new BasicAuthRequestInterceptor("somas", "password"))
                .target(LoadBalancingTarget.create(PropertiesCtrl.class, "http://client"));

        //Properties properties = propertiesCtrl.properties("amazon", "integration");
        Properties propertiesB = propertiesCtrlB.properties("amazon", "integration");

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ConfigClientMain.class);
        ctx.refresh();
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

/*
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Basic integration for {@link com.netflix.loadbalancer.ILoadBalancer loadbalancer-aware} targets.
 * Using this will enable dynamic url discovery via ribbon including incrementing server request
 * counts. <br> Ex.
 * <pre>
 * MyService api = Feign.builder().target(LoadBalancingTarget.create(MyService.class,
 * "http://myAppProd"))
 * </pre>
 * Where {@code myAppProd} is the ribbon loadbalancer name and {@code
 * myAppProd.ribbon.listOfServers} configuration is set.
 *
 * @param <T> corresponds to {@link feign.Target#type()}
 */
 class MyLoadBalancingTarget<T> implements Target<T> {

    private final String name;
    private final String scheme;
    private final Class<T> type;
    private final AbstractLoadBalancer lb;
    protected MyLoadBalancingTarget(Class<T> type, String scheme, String name, AbstractLoadBalancer lb) {
        this.type = checkNotNull(type, "type");
        this.scheme = checkNotNull(scheme, "scheme");
        this.name = checkNotNull(name, "name");
        this.lb = lb;//AbstractLoadBalancer.class.cast(getNamedLoadBalancer(name()));
    }

    /**
     * creates a target which dynamically derives urls from a {@link com.netflix.loadbalancer.ILoadBalancer
     * loadbalancer}.
     *
     * @param type       corresponds to {@link feign.Target#type()}
     * @param schemeName naming convention is {@code https://name} or {@code http://name} where name
     *                   corresponds to {@link com.netflix.client.ClientFactory#getNamedLoadBalancer(String)}
     */
    public static <T> MyLoadBalancingTarget<T> create(Class<T> type, String schemeName, AbstractLoadBalancer lb) {
        URI asUri = URI.create(schemeName);
        return new MyLoadBalancingTarget<T>(type, asUri.getScheme(), asUri.getHost(), lb);
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String url() {
        return name;
    }

    /**
     * current load balancer for the target.
     */
    public AbstractLoadBalancer lb() {
        return lb;
    }

    @Override
    public Request apply(RequestTemplate input) {
        Server currentServer = lb.chooseServer(null);
        String url = format("%s://%s", scheme, currentServer.getHostPort());
        input.insert(0, url);
        try {
            return input.request();
        } finally {
            lb.getLoadBalancerStats().incrementNumRequests(currentServer);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LoadBalancingTarget) {
            MyLoadBalancingTarget<?> other = (MyLoadBalancingTarget<?>) obj;
            return type.equals(other.type)
                    && name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LoadBalancingTarget(type=" + type.getSimpleName() + ", name=" + name + ")";
    }
}
