package com.mynegocio.app.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.jhipster.config.JHipsterProperties;
import java.util.concurrent.TimeUnit;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.starter.embedded.InfinispanCacheConfigurer;
import org.infinispan.spring.starter.embedded.InfinispanEmbeddedCacheManagerAutoConfiguration;
import org.infinispan.spring.starter.embedded.InfinispanGlobalConfigurer;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.jcache.embedded.ConfigurationAdapter;
import org.infinispan.jcache.embedded.JCache;
import org.infinispan.jcache.embedded.JCacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.PhysicalAddress;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.ProtocolStack;
import java.net.InetAddress;
import org.springframework.beans.factory.BeanInitializationException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
@Import(InfinispanEmbeddedCacheManagerAutoConfiguration.class)
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    // Initialize the cache in a non Spring-managed bean
    private static EmbeddedCacheManager cacheManager;

    private DiscoveryClient discoveryClient;

    private Registration registration;

    @Autowired(required = false)
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    @Autowired(required = false)
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public static EmbeddedCacheManager getCacheManager(){
        return cacheManager;
    }

    public static void setCacheManager(EmbeddedCacheManager cacheManager) {
        CacheConfiguration.cacheManager = cacheManager;
    }

    /**
     * Inject a {@link org.infinispan.configuration.global.GlobalConfiguration GlobalConfiguration} for Infinispan cache.
     * <p>
     * If a service discovery solution is enabled (JHipster Registry or Consul),
     * then the host list will be populated from the service discovery.
     *
     * <p>
     * If the service discovery is not enabled, host discovery will be based on
     * the default transport settings defined in the 'config-file' packaged within
     * the Jar. The 'config-file' can be overridden using the application property
     * <i>jhipster.cache.infinispan.config-file</i>
     *
     * <p>
     * If no service discovery is defined, you have the choice of 'config-file'
     * based on the underlying platform for hosts discovery. Infinispan
     * supports discovery natively for most of the platforms like Kubernetes/OpenShift,
     * AWS, Azure and Google.
     *
     * @param jHipsterProperties the jhipster properties to configure from.
     * @return the infinispan global configurer.
     */
    @Bean
    public InfinispanGlobalConfigurer globalConfiguration(JHipsterProperties jHipsterProperties) {
        log.info("Defining Infinispan Global Configuration");
            if (this.registration == null) { // if registry is not defined, use native discovery
                log.warn("No discovery service is set up, Infinispan will use default discovery for cluster formation");
                return () -> GlobalConfigurationBuilder
                    .defaultClusteredBuilder().defaultCacheName("infinispan-hjisterapp1-cluster-cache").transport().defaultTransport()
                    .addProperty("configurationFile", jHipsterProperties.getCache().getInfinispan().getConfigFile())
                    .clusterName("infinispan-hjisterapp1-cluster").globalJmxStatistics()
                    .enabled(jHipsterProperties.getCache().getInfinispan().isStatsEnabled())
                    .allowDuplicateDomains(true).build();
            }
            return () -> GlobalConfigurationBuilder
                    .defaultClusteredBuilder().defaultCacheName("infinispan-hjisterapp1-cluster-cache").transport()
                    .transport(new JGroupsTransport(getTransportChannel()))
                    .clusterName("infinispan-hjisterapp1-cluster").globalJmxStatistics()
                    .enabled(jHipsterProperties.getCache().getInfinispan().isStatsEnabled())
                    .allowDuplicateDomains(true).build();
    }

    /**
     * Initialize cache configuration for Hibernate L2 cache and Spring Cache.
     * <p>
     * There are three different modes: local, distributed and replicated, and L2 cache options are pre-configured.
     *
     * <p>
     * It supports both jCache and Spring cache abstractions.
     * <p>
     * Usage:
     *  <ol>
     *      <li>
     *          jCache:
     *          <pre class="code">@CacheResult(cacheName = "dist-app-data") </pre>
     *              - for creating a distributed cache. In a similar way other cache names and options can be used
     *      </li>
     *      <li>
     *          Spring Cache:
     *          <pre class="code">@Cacheable(value = "repl-app-data") </pre>
     *              - for creating a replicated cache. In a similar way other cache names and options can be used
     *      </li>
     *      <li>
     *          Cache manager can also be injected through DI/CDI and data can be manipulated using Infinispan APIs,
     *          <pre class="code">
     *          &#064;Autowired (or) &#064;Inject
     *          private EmbeddedCacheManager cacheManager;
     *
     *          void cacheSample(){
     *              cacheManager.getCache("dist-app-data").put("hi", "there");
     *          }
     *          </pre>
     *      </li>
     *  </ol>
     *
     * @param jHipsterProperties the jhipster properties to configure from.
     * @return the infinispan cache configurer.
     */
    @Bean
    public InfinispanCacheConfigurer cacheConfigurer(JHipsterProperties jHipsterProperties) {
        log.info("Defining {} configuration", "app-data for local, replicated and distributed modes");
        JHipsterProperties.Cache.Infinispan cacheInfo = jHipsterProperties.getCache().getInfinispan();

        return manager -> {
            // initialize application cache
            manager.defineConfiguration("local-app-data", new ConfigurationBuilder()
                .clustering().cacheMode(CacheMode.LOCAL)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .memory().evictionType(EvictionType.COUNT).size(cacheInfo.getLocal().getMaxEntries()).expiration()
                .lifespan(cacheInfo.getLocal().getTimeToLiveSeconds(), TimeUnit.SECONDS).build());
            manager.defineConfiguration("dist-app-data", new ConfigurationBuilder()
                .clustering().cacheMode(CacheMode.DIST_SYNC).hash().numOwners(cacheInfo.getDistributed().getInstanceCount())
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .memory().evictionType(EvictionType.COUNT).size(cacheInfo.getDistributed().getMaxEntries()).expiration()
                .lifespan(cacheInfo.getDistributed().getTimeToLiveSeconds(), TimeUnit.SECONDS).build());
            manager.defineConfiguration("repl-app-data", new ConfigurationBuilder()
                .clustering().cacheMode(CacheMode.REPL_SYNC)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .memory().evictionType(EvictionType.COUNT).size(cacheInfo.getReplicated().getMaxEntries()).expiration()
                .lifespan(cacheInfo.getReplicated().getTimeToLiveSeconds(), TimeUnit.SECONDS).build());

            // initialize Hibernate L2 cache
            manager.defineConfiguration("entity", new ConfigurationBuilder().clustering().cacheMode(CacheMode.INVALIDATION_SYNC)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("replicated-entity", new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_SYNC)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("local-query", new ConfigurationBuilder().clustering().cacheMode(CacheMode.LOCAL)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("replicated-query", new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_ASYNC)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("timestamps", new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_ASYNC)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("pending-puts", new ConfigurationBuilder().clustering().cacheMode(CacheMode.LOCAL)
                .jmxStatistics().enabled(cacheInfo.isStatsEnabled())
                .simpleCache(true).transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL).expiration().maxIdle(60000).build());

            setCacheManager(manager);
        };
    }

    /**
     * <p>
     * Instance of {@link JCacheManager} with cache being managed by the underlying Infinispan layer. This helps to record stats
     * info if enabled and the same is accessible through {@code MBX:javax.cache,type=CacheStatistics}.
     *
     * <p>
     * jCache stats are at instance level. If you need stats at clustering level, then it needs to be retrieved from {@code MBX:org.infinispan}
     *
     * @param cacheManager the embedded cache manager.
     * @param jHipsterProperties the jhipster properties to configure from.
     * @return the jcache manager.
     */
    @Bean
    public JCacheManager getJCacheManager(EmbeddedCacheManager cacheManager, JHipsterProperties jHipsterProperties){
        return new InfinispanJCacheManager(Caching.getCachingProvider().getDefaultURI(), cacheManager,
            Caching.getCachingProvider(), jHipsterProperties);
    }

    class InfinispanJCacheManager extends JCacheManager {

        public InfinispanJCacheManager(URI uri, EmbeddedCacheManager cacheManager, CachingProvider provider,
                                       JHipsterProperties jHipsterProperties) {
            super(uri, cacheManager, provider);
            // register individual caches to make the stats info available.
            registerPredefinedCache(com.mynegocio.app.repository.UserRepository.USERS_BY_LOGIN_CACHE, new JCache<Object, Object>(
                cacheManager.getCache(com.mynegocio.app.repository.UserRepository.USERS_BY_LOGIN_CACHE).getAdvancedCache(), this,
                ConfigurationAdapter.create()));
            registerPredefinedCache(com.mynegocio.app.repository.UserRepository.USERS_BY_EMAIL_CACHE, new JCache<Object, Object>(
                cacheManager.getCache(com.mynegocio.app.repository.UserRepository.USERS_BY_EMAIL_CACHE).getAdvancedCache(), this,
                ConfigurationAdapter.create()));
            registerPredefinedCache(com.mynegocio.app.domain.User.class.getName(), new JCache<Object, Object>(
                cacheManager.getCache(com.mynegocio.app.domain.User.class.getName()).getAdvancedCache(), this,
                ConfigurationAdapter.create()));
            registerPredefinedCache(com.mynegocio.app.domain.Authority.class.getName(), new JCache<Object, Object>(
                cacheManager.getCache(com.mynegocio.app.domain.Authority.class.getName()).getAdvancedCache(), this,
                ConfigurationAdapter.create()));
            registerPredefinedCache(com.mynegocio.app.domain.User.class.getName() + ".authorities", new JCache<Object, Object>(
                cacheManager.getCache(com.mynegocio.app.domain.User.class.getName() + ".authorities").getAdvancedCache(), this,
                ConfigurationAdapter.create()));
            // jhipster-needle-infinispan-add-entry
            if (jHipsterProperties.getCache().getInfinispan().isStatsEnabled()) {
                for (String cacheName : cacheManager.getCacheNames()) {
                    enableStatistics(cacheName, true);
                }
            }
        }
    }

    /**
     * TCP channel with the host details populated from the service discovery
     * (JHipster Registry or Consul).
     * <p>
     * MPING multicast is replaced with TCPPING with the host details discovered
     * from registry and sends only unicast messages to the host list.
     */
    private JChannel getTransportChannel() {
        JChannel channel = null;
        List<PhysicalAddress> initialHosts = new ArrayList<>();
        try {
            for (ServiceInstance instance : discoveryClient.getInstances(registration.getServiceId())) {
                String clusterMember = instance.getHost() + ":7800";
                log.debug("Adding Infinispan cluster member {}", clusterMember);
                initialHosts.add(new IpAddress(clusterMember));
            }
            TCP tcp = new TCP();
            tcp.setBindAddress(InetAddress.getLocalHost());
            tcp.setBindPort(7800);
            tcp.setThreadPoolMinThreads(2);
            tcp.setThreadPoolMaxThreads(30);
            tcp.setThreadPoolKeepAliveTime(60000);

            TCPPING tcpping = new TCPPING();
            initialHosts.add(new IpAddress(InetAddress.getLocalHost(), 7800));
            tcpping.setInitialHosts(initialHosts);
            tcpping.setErgonomics(false);
            tcpping.setPortRange(10);
            tcpping.sendCacheInformation();

            NAKACK2 nakack = new NAKACK2();
            nakack.setUseMcastXmit(false);
            nakack.setDiscardDeliveredMsgs(false);

            MERGE3 merge = new MERGE3();
            merge.setMinInterval(10000);
            merge.setMaxInterval(30000);

            FD_ALL fd = new FD_ALL();
            fd.setTimeout(60000);
            fd.setInterval(15000);
            fd.setTimeoutCheckInterval(5000);

            ProtocolStack stack = new ProtocolStack();
            // Order shouldn't be changed
            stack
                .addProtocol(tcp)
                .addProtocol(tcpping)
                .addProtocol(merge)
                .addProtocol(new FD_SOCK())
                .addProtocol(fd)
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(nakack)
                .addProtocol(new UNICAST3())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2());
            channel = new JChannel(stack);
            stack.init();
        } catch (Exception e) {
            throw new BeanInitializationException("Cache (Infinispan protocol stack) configuration failed", e);
        }
        return channel;
    }
}
