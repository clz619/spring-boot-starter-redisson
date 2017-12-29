package win.sinno.spring.boot.starter.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import win.sinno.spring.boot.starter.redisson.RedissonConfigurationProperties.Cluster;
import win.sinno.spring.boot.starter.redisson.RedissonConfigurationProperties.MasterSlave;
import win.sinno.spring.boot.starter.redisson.RedissonConfigurationProperties.Sentinel;
import win.sinno.spring.boot.starter.redisson.RedissonConfigurationProperties.Single;

/**
 * win.sinno.spring.boot.starter.redisson.RedissonAutoConfiguration
 *
 * @author chenlizhong@qipeng.com
 * @date 2017/12/29
 */

@Configuration
@ConditionalOnClass({Redisson.class})
@ConditionalOnMissingBean(RedissonClient.class)
@ConditionalOnProperty(prefix = "spring.redisson", name = "type")
@EnableConfigurationProperties(RedissonConfigurationProperties.class)
public class RedissonAutoConfiguration {

  @Autowired
  private RedissonConfigurationProperties redissonConfigurationProperties;

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {
    Config config = new Config();

    configBaisc(config);
    switch (redissonConfigurationProperties.getType()) {
      case SINGLE:
        configSingle(config);
        break;
      case CLUSTER:
        configCluster(config);
        break;
      case SENTINEL:
        configSentinel(config);
        break;
      case MASTER_SLAVE:
        configMasterSlave(config);
        break;
      default:
        throw new IllegalArgumentException(
            "illegal parameter [type] : " + redissonConfigurationProperties.getType());
    }
    return Redisson.create(config);
  }

  private void configBaisc(Config config) {
    config.setThreads(redissonConfigurationProperties.getThreads())
        .setNettyThreads(redissonConfigurationProperties.getNettyThreads())
        .setCodec(redissonConfigurationProperties.getCodec())
        .setReferenceCodecProvider(redissonConfigurationProperties.getReferenceCodecProvider())
        .setExecutor(redissonConfigurationProperties.getExecutor())

    ;
    config.setReferenceEnabled(redissonConfigurationProperties.isReferenceEnabled());
    config.setUseLinuxNativeEpoll(redissonConfigurationProperties.isUseLinuxNativeEpoll());
    config.setEventLoopGroup(redissonConfigurationProperties.getEventLoopGroup());
    config.setLockWatchdogTimeout(redissonConfigurationProperties.getLockWatchdogTimeout());
    config.setKeepPubSubOrder(redissonConfigurationProperties.isKeepPubSubOrder());
  }

  private void configSingle(Config config) {

    Single conf = redissonConfigurationProperties.getSingle();

    config.useSingleServer()
        .setIdleConnectionTimeout(
            conf.getIdleConnectionTimeout())
        .setPingTimeout(conf.getPingTimeout())
        .setConnectTimeout(conf.getConnectTimeout())
        .setTimeout(conf.getTimeout())
        .setRetryAttempts(conf.getRetryAttempts())
        .setRetryInterval(conf.getRetryInterval())
        .setReconnectionTimeout(conf.getReconnectionTimeout())
        .setFailedAttempts(conf.getFailedAttempts())
        .setPassword(conf.getPassword())
        .setSubscriptionsPerConnection(conf.getSubscriptionsPerConnection())
        .setClientName(conf.getClientName())
        .setSslEnableEndpointIdentification(conf.isSslEnableEndpointIdentification())
        .setSslProvider(conf.getSslProvider())
        .setSslTruststore(conf.getSslTruststore())
        .setSslKeystorePassword(conf.getSslKeystorePassword())
        .setPingConnectionInterval(conf.getPingConnectionInterval())
        .setKeepAlive(conf.isKeepAlive())
        .setTcpNoDelay(conf.isTcpNoDelay())

        .setAddress(conf.getAddress())
        .setSubscriptionConnectionMinimumIdleSize(conf.getSubscriptionConnectionMinimumIdleSize())
        .setSubscriptionConnectionPoolSize(conf.getSubscriptionConnectionPoolSize())
        .setConnectionMinimumIdleSize(conf.getConnectionMinimumIdleSize())
        .setConnectionPoolSize(conf.getConnectionPoolSize())
        .setDatabase(conf.getDatabase())
        .setDnsMonitoring(conf.isDnsMonitoring())
        .setDnsMonitoringInterval(conf.getDnsMonitoringInterval())
    ;
  }

  private void configCluster(Config config) {

    Cluster conf = redissonConfigurationProperties.getCluster();
    config.useClusterServers()
        .setIdleConnectionTimeout(
            conf.getIdleConnectionTimeout())
        .setPingTimeout(conf.getPingTimeout())
        .setConnectTimeout(conf.getConnectTimeout())
        .setTimeout(conf.getTimeout())
        .setRetryAttempts(conf.getRetryAttempts())
        .setRetryInterval(conf.getRetryInterval())
        .setReconnectionTimeout(conf.getReconnectionTimeout())
        .setFailedAttempts(conf.getFailedAttempts())
        .setPassword(conf.getPassword())
        .setSubscriptionsPerConnection(conf.getSubscriptionsPerConnection())
        .setClientName(conf.getClientName())
        .setSslEnableEndpointIdentification(conf.isSslEnableEndpointIdentification())
        .setSslProvider(conf.getSslProvider())
        .setSslTruststore(conf.getSslTruststore())
        .setSslKeystorePassword(conf.getSslKeystorePassword())
        .setPingConnectionInterval(conf.getPingConnectionInterval())
        .setKeepAlive(conf.isKeepAlive())
        .setTcpNoDelay(conf.isTcpNoDelay())

        .setLoadBalancer(conf.getLoadBalancer())
        .setSlaveConnectionMinimumIdleSize(conf.getSlaveConnectionMinimumIdleSize())
        .setSlaveConnectionPoolSize(conf.getSlaveConnectionPoolSize())
        .setMasterConnectionMinimumIdleSize(conf.getMasterConnectionMinimumIdleSize())
        .setMasterConnectionPoolSize(conf.getMasterConnectionPoolSize())
        .setReadMode(conf.getReadMode())
        .setSubscriptionMode(conf.getSubscriptionMode())
        .setSubscriptionConnectionMinimumIdleSize(conf.getSubscriptionConnectionMinimumIdleSize())
        .setSubscriptionConnectionPoolSize(conf.getSubscriptionConnectionPoolSize())
        .setDnsMonitoringInterval(conf.getDnsMonitoringInterval())

        .addNodeAddress(conf.getNodeAddressed())
        .setScanInterval(conf.getScanInterval())

    ;
  }

  private void configSentinel(Config config) {
    Sentinel conf = redissonConfigurationProperties.getSentinel();
    config.useSentinelServers()
        .setIdleConnectionTimeout(
            conf.getIdleConnectionTimeout())
        .setPingTimeout(conf.getPingTimeout())
        .setConnectTimeout(conf.getConnectTimeout())
        .setTimeout(conf.getTimeout())
        .setRetryAttempts(conf.getRetryAttempts())
        .setRetryInterval(conf.getRetryInterval())
        .setReconnectionTimeout(conf.getReconnectionTimeout())
        .setFailedAttempts(conf.getFailedAttempts())
        .setPassword(conf.getPassword())
        .setSubscriptionsPerConnection(conf.getSubscriptionsPerConnection())
        .setClientName(conf.getClientName())
        .setSslEnableEndpointIdentification(conf.isSslEnableEndpointIdentification())
        .setSslProvider(conf.getSslProvider())
        .setSslTruststore(conf.getSslTruststore())
        .setSslKeystorePassword(conf.getSslKeystorePassword())
        .setPingConnectionInterval(conf.getPingConnectionInterval())
        .setKeepAlive(conf.isKeepAlive())
        .setTcpNoDelay(conf.isTcpNoDelay())

        .setLoadBalancer(conf.getLoadBalancer())
        .setSlaveConnectionMinimumIdleSize(conf.getSlaveConnectionMinimumIdleSize())
        .setSlaveConnectionPoolSize(conf.getSlaveConnectionPoolSize())
        .setMasterConnectionMinimumIdleSize(conf.getMasterConnectionMinimumIdleSize())
        .setMasterConnectionPoolSize(conf.getMasterConnectionPoolSize())
        .setReadMode(conf.getReadMode())
        .setSubscriptionMode(conf.getSubscriptionMode())
        .setSubscriptionConnectionMinimumIdleSize(conf.getSubscriptionConnectionMinimumIdleSize())
        .setSubscriptionConnectionPoolSize(conf.getSubscriptionConnectionPoolSize())
        .setDnsMonitoringInterval(conf.getDnsMonitoringInterval())

        .setMasterName(conf.getMasterName())
        .addSentinelAddress(conf.getSentinelAddresses())
        .setDatabase(conf.getDatabase())

    ;
  }

  private void configMasterSlave(Config config) {

    MasterSlave conf = redissonConfigurationProperties.getMasterSlave();
    config.useMasterSlaveServers()
        .setIdleConnectionTimeout(
            conf.getIdleConnectionTimeout())
        .setPingTimeout(conf.getPingTimeout())
        .setConnectTimeout(conf.getConnectTimeout())
        .setTimeout(conf.getTimeout())
        .setRetryAttempts(conf.getRetryAttempts())
        .setRetryInterval(conf.getRetryInterval())
        .setReconnectionTimeout(conf.getReconnectionTimeout())
        .setFailedAttempts(conf.getFailedAttempts())
        .setPassword(conf.getPassword())
        .setSubscriptionsPerConnection(conf.getSubscriptionsPerConnection())
        .setClientName(conf.getClientName())
        .setSslEnableEndpointIdentification(conf.isSslEnableEndpointIdentification())
        .setSslProvider(conf.getSslProvider())
        .setSslTruststore(conf.getSslTruststore())
        .setSslKeystorePassword(conf.getSslKeystorePassword())
        .setPingConnectionInterval(conf.getPingConnectionInterval())
        .setKeepAlive(conf.isKeepAlive())
        .setTcpNoDelay(conf.isTcpNoDelay())

        .setLoadBalancer(conf.getLoadBalancer())
        .setSlaveConnectionMinimumIdleSize(conf.getSlaveConnectionMinimumIdleSize())
        .setSlaveConnectionPoolSize(conf.getSlaveConnectionPoolSize())
        .setMasterConnectionMinimumIdleSize(conf.getMasterConnectionMinimumIdleSize())
        .setMasterConnectionPoolSize(conf.getMasterConnectionPoolSize())
        .setReadMode(conf.getReadMode())
        .setSubscriptionMode(conf.getSubscriptionMode())
        .setSubscriptionConnectionMinimumIdleSize(conf.getSubscriptionConnectionMinimumIdleSize())
        .setSubscriptionConnectionPoolSize(conf.getSubscriptionConnectionPoolSize())
        .setDnsMonitoringInterval(conf.getDnsMonitoringInterval())

        .setMasterAddress(conf.getMasterAddress())
        .addSlaveAddress(conf.getSlaveAddresses())
        .setDatabase(conf.getDatabase())

    ;

  }


}
