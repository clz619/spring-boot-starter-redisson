package win.sinno.spring.boot.starter.redisson;

import io.netty.channel.EventLoopGroup;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import lombok.Getter;
import lombok.Setter;
import org.redisson.client.codec.Codec;
import org.redisson.codec.DefaultReferenceCodecProvider;
import org.redisson.codec.ReferenceCodecProvider;
import org.redisson.config.ReadMode;
import org.redisson.config.SslProvider;
import org.redisson.config.SubscriptionMode;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * win.sinno.spring.boot.starter.redisson.RedissonConfigurationProperties
 *
 * @author chenlizhong@qipeng.com
 * @date 2017/12/29
 */
@ConfigurationProperties("spring.redisson")
@Getter
@Setter
public class RedissonConfigurationProperties {

  private Integer threads = 0;
  private Integer nettyThreads = 0;
  private Codec codec;
  private ReferenceCodecProvider referenceCodecProvider = new DefaultReferenceCodecProvider();
  private ExecutorService executor;

  private boolean referenceEnabled = true;

  private boolean useLinuxNativeEpoll;

  private EventLoopGroup eventLoopGroup;

  private long lockWatchdogTimeout = 30 * 1000;

  private boolean keepPubSubOrder = true;


  private RedissonType type = RedissonType.SINGLE;

  @NestedConfigurationProperty
  private Single single = new Single();

  @NestedConfigurationProperty
  private Cluster cluster = new Cluster();

  @NestedConfigurationProperty
  private Sentinel sentinel = new Sentinel();

  @NestedConfigurationProperty
  private MasterSlave masterSlave = new MasterSlave();

  public enum RedissonType {
    SINGLE, CLUSTER, SENTINEL, MASTER_SLAVE
  }

  @Getter
  @Setter
  public static class BaseConf {

    /**
     * If pooled connection not used for a <code>timeout</code> time and current connections amount
     * bigger than minimum idle connections pool size, then it will closed and removed from pool.
     * Value in milliseconds.
     */
    private int idleConnectionTimeout = 10000;

    /**
     * Ping timeout used in <code>Node.ping</code> and <code>Node.pingAll<code> operation. Value in
     * milliseconds.
     */
    private int pingTimeout = 1000;

    /**
     * Timeout during connecting to any Redis server. Value in milliseconds.
     */
    private int connectTimeout = 10000;

    /**
     * Redis server response timeout. Starts to countdown when Redis command was succesfully sent.
     * Value in milliseconds.
     */
    private int timeout = 3000;

    private int retryAttempts = 3;

    private int retryInterval = 1500;

    /**
     * Reconnection attempt timeout to Redis server then it has been excluded from internal list of
     * available servers.
     *
     * On every such timeout event Redisson tries to connect to disconnected Redis server.
     *
     * @see #failedAttempts
     */
    private int reconnectionTimeout = 3000;


    private int failedAttempts = 3;


    private String password;

    private int subscriptionsPerConnection = 5;

    private String clientName;

    private boolean sslEnableEndpointIdentification = true;

    private SslProvider sslProvider = SslProvider.JDK;

    private URI sslTruststore;

    private String sslTruststorePassword;

    private URI sslKeystore;

    private String sslKeystorePassword;

    private int pingConnectionInterval;

    private boolean keepAlive;

    private boolean tcpNoDelay;
  }

  @Getter
  @Setter
  public static class Single extends BaseConf {

    private String address = "redis://127.0.0.1:6397";

    /**
     * Minimum idle subscription connection amount
     */
    private int subscriptionConnectionMinimumIdleSize = 1;

    /**
     * Redis subscription connection maximum pool size
     */
    private int subscriptionConnectionPoolSize = 50;

    /**
     * Minimum idle Redis connection amount
     */
    private int connectionMinimumIdleSize = 10;

    /**
     * Redis connection maximum pool size
     */
    private int connectionPoolSize = 64;

    /**
     * Database index used for Redis connection
     */
    private int database = 0;

    /**
     * Should the server address be monitored for changes in DNS? Useful for AWS ElastiCache where
     * the client is pointed at the endpoint for a replication group which is a DNS alias to the
     * current master node.<br> <em>NB: applications must ensure the JVM DNS cache TTL is low enough
     * to support this.</em> e.g., http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/java-dg-jvm-ttl.html
     */
    private boolean dnsMonitoring = true;

    /**
     * Interval in milliseconds to check DNS
     */
    private long dnsMonitoringInterval = 5000;

  }

  @Getter
  @Setter
  public static class BaseMasterSlaveConf extends BaseConf {

    /**
     * Сonnection load balancer for multiple Redis slave servers
     */
    private LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    /**
     * Redis 'slave' node minimum idle connection amount for <b>each</b> slave node
     */
    private int slaveConnectionMinimumIdleSize = 10;

    /**
     * Redis 'slave' node maximum connection pool size for <b>each</b> slave node
     */
    private int slaveConnectionPoolSize = 64;

    /**
     * Redis 'master' node minimum idle connection amount for <b>each</b> slave node
     */
    private int masterConnectionMinimumIdleSize = 10;

    /**
     * Redis 'master' node maximum connection pool size
     */
    private int masterConnectionPoolSize = 64;

    private ReadMode readMode = ReadMode.SLAVE;

    private SubscriptionMode subscriptionMode = SubscriptionMode.SLAVE;

    /**
     * Redis 'slave' node minimum idle subscription (pub/sub) connection amount for <b>each</b>
     * slave node
     */
    private int subscriptionConnectionMinimumIdleSize = 1;

    /**
     * Redis 'slave' node maximum subscription (pub/sub) connection pool size for <b>each</b> slave
     * node
     */
    private int subscriptionConnectionPoolSize = 50;

    private long dnsMonitoringInterval = 5000;

  }

  @Getter
  @Setter
  public static class Cluster extends BaseMasterSlaveConf {

    private String[] nodeAddressed;
    private int scanInterval = 1000;

  }

  @Getter
  @Setter
  public static class Sentinel extends BaseMasterSlaveConf {

    private String masterName;
    private String[] sentinelAddresses;
    private int database = 0;

  }

  @Getter
  @Setter
  public static class MasterSlave extends BaseMasterSlaveConf {

    private String masterAddress;
    private String[] slaveAddresses;
    private int database = 0;

  }
}
