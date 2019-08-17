//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.netflix.zuul.filters;

import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import com.util.DomXmlUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@ConfigurationProperties("zuul")
public class ZuulProperties {
    public static final List<String> SECURITY_HEADERS = Arrays.asList("Pragma", "Cache-Control", "X-Frame-Options", "X-Content-Type-Options", "X-XSS-Protection", "Expires");
    private String prefix = "";
    private boolean stripPrefix = true;
    private Boolean retryable = false;
    private Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap();
    private boolean addProxyHeaders = true;
    private boolean addHostHeader = false;
    private Set<String> ignoredServices = new LinkedHashSet();
    private Set<String> ignoredPatterns = new LinkedHashSet();
    private Set<String> ignoredHeaders = new LinkedHashSet();
    private boolean ignoreSecurityHeaders = true;
    private boolean forceOriginalQueryStringEncoding = false;
    private String servletPath = "/zuul";
    private boolean ignoreLocalService = true;
    private ZuulProperties.Host host = new ZuulProperties.Host();
    private boolean traceRequestBody = true;
    private boolean removeSemicolonContent = true;
    private boolean decodeUrl = true;
    private Set<String> sensitiveHeaders = new LinkedHashSet(Arrays.asList("Cookie", "Set-Cookie", "Authorization"));
    private boolean sslHostnameValidationEnabled = true;
    private ExecutionIsolationStrategy ribbonIsolationStrategy;
    private ZuulProperties.HystrixSemaphore semaphore;
    private ZuulProperties.HystrixThreadPool threadPool;
    private boolean setContentLength;
    private boolean includeDebugHeader;
    private int initialStreamBufferSize;

    public ZuulProperties() {
        this.ribbonIsolationStrategy = ExecutionIsolationStrategy.SEMAPHORE;
        this.semaphore = new ZuulProperties.HystrixSemaphore();
        this.threadPool = new ZuulProperties.HystrixThreadPool();
        this.setContentLength = false;
        this.includeDebugHeader = false;
        this.initialStreamBufferSize = 8192;
    }

    public Set<String> getIgnoredHeaders() {
        Set<String> ignoredHeaders = new LinkedHashSet(this.ignoredHeaders);
        if (ClassUtils.isPresent("org.springframework.security.config.annotation.web.WebSecurityConfigurer", (ClassLoader)null) && Collections.disjoint(ignoredHeaders, SECURITY_HEADERS) && this.ignoreSecurityHeaders) {
            ignoredHeaders.addAll(SECURITY_HEADERS);
        }

        return ignoredHeaders;
    }

    public void setIgnoredHeaders(Set<String> ignoredHeaders) {
        this.ignoredHeaders.addAll(ignoredHeaders);
    }

    @PostConstruct
    public void init() {
        Iterator var1 = this.routes.entrySet().iterator();

        while(var1.hasNext()) {
            Entry<String, ZuulProperties.ZuulRoute> entry = (Entry)var1.next();
            ZuulProperties.ZuulRoute value = (ZuulProperties.ZuulRoute)entry.getValue();
            if (!StringUtils.hasText(value.getLocation())) {
                value.serviceId = (String)entry.getKey();
            }

            if (!StringUtils.hasText(value.getId())) {
                value.id = (String)entry.getKey();
            }

            if (!StringUtils.hasText(value.getPath())) {
                value.path = "/" + (String)entry.getKey() + "/**";
            }
        }

    }

    public String getServletPattern() {
        String path = this.servletPath;
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (!path.contains("*")) {
            path = path.endsWith("/") ? path + "*" : path + "/*";
        }

        return path;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isStripPrefix() {
        return this.stripPrefix;
    }

    public void setStripPrefix(boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public Boolean getRetryable() {
        return this.retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public Map<String, ZuulProperties.ZuulRoute> getRoutes() {
        return this.routes;
    }

    public void setRoutes(Map<String, ZuulProperties.ZuulRoute> routes) {
        getRoutesFromXml(routes);
        this.routes = routes;
    }

    private void getRoutesFromXml(Map<String, ZuulProperties.ZuulRoute> routeList) {
        String modulePath = null;
        try {
            modulePath = new File("").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        DomXmlUtils domXmlUtils = new DomXmlUtils(new File(modulePath + "/routes.xml"));
        domXmlUtils.parseXml(new DomXmlUtils.BaseDomXmlParser<Object>() {
            @Override
            protected Object parse(Document doc) {
                Element root = doc.getDocumentElement();
                NodeList routes = root.getElementsByTagName("route");
                for (int i = 0; i < routes.getLength(); i++) {
                    Element route = (Element) routes.item(i);
                    String serviceId=route.getAttribute("serviceId");
                    routeList.put(serviceId,new ZuulProperties.ZuulRoute(null,route.getAttribute("path"),serviceId,null,true,null,new HashSet<>()));
                }
                return null;
            }
        });
    }

    public boolean isAddProxyHeaders() {
        return this.addProxyHeaders;
    }

    public void setAddProxyHeaders(boolean addProxyHeaders) {
        this.addProxyHeaders = addProxyHeaders;
    }

    public boolean isAddHostHeader() {
        return this.addHostHeader;
    }

    public void setAddHostHeader(boolean addHostHeader) {
        this.addHostHeader = addHostHeader;
    }

    public Set<String> getIgnoredServices() {
        return this.ignoredServices;
    }

    public void setIgnoredServices(Set<String> ignoredServices) {
        this.ignoredServices = ignoredServices;
    }

    public Set<String> getIgnoredPatterns() {
        return this.ignoredPatterns;
    }

    public void setIgnoredPatterns(Set<String> ignoredPatterns) {
        this.ignoredPatterns = ignoredPatterns;
    }

    public boolean isIgnoreSecurityHeaders() {
        return this.ignoreSecurityHeaders;
    }

    public void setIgnoreSecurityHeaders(boolean ignoreSecurityHeaders) {
        this.ignoreSecurityHeaders = ignoreSecurityHeaders;
    }

    public boolean isForceOriginalQueryStringEncoding() {
        return this.forceOriginalQueryStringEncoding;
    }

    public void setForceOriginalQueryStringEncoding(boolean forceOriginalQueryStringEncoding) {
        this.forceOriginalQueryStringEncoding = forceOriginalQueryStringEncoding;
    }

    public String getServletPath() {
        return this.servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public boolean isIgnoreLocalService() {
        return this.ignoreLocalService;
    }

    public void setIgnoreLocalService(boolean ignoreLocalService) {
        this.ignoreLocalService = ignoreLocalService;
    }

    public ZuulProperties.Host getHost() {
        return this.host;
    }

    public void setHost(ZuulProperties.Host host) {
        this.host = host;
    }

    public boolean isTraceRequestBody() {
        return this.traceRequestBody;
    }

    public void setTraceRequestBody(boolean traceRequestBody) {
        this.traceRequestBody = traceRequestBody;
    }

    public boolean isRemoveSemicolonContent() {
        return this.removeSemicolonContent;
    }

    public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
        this.removeSemicolonContent = removeSemicolonContent;
    }

    public boolean isDecodeUrl() {
        return this.decodeUrl;
    }

    public void setDecodeUrl(boolean decodeUrl) {
        this.decodeUrl = decodeUrl;
    }

    public Set<String> getSensitiveHeaders() {
        return this.sensitiveHeaders;
    }

    public void setSensitiveHeaders(Set<String> sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    public boolean isSslHostnameValidationEnabled() {
        return this.sslHostnameValidationEnabled;
    }

    public void setSslHostnameValidationEnabled(boolean sslHostnameValidationEnabled) {
        this.sslHostnameValidationEnabled = sslHostnameValidationEnabled;
    }

    public ExecutionIsolationStrategy getRibbonIsolationStrategy() {
        return this.ribbonIsolationStrategy;
    }

    public void setRibbonIsolationStrategy(ExecutionIsolationStrategy ribbonIsolationStrategy) {
        this.ribbonIsolationStrategy = ribbonIsolationStrategy;
    }

    public ZuulProperties.HystrixSemaphore getSemaphore() {
        return this.semaphore;
    }

    public void setSemaphore(ZuulProperties.HystrixSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public ZuulProperties.HystrixThreadPool getThreadPool() {
        return this.threadPool;
    }

    public void setThreadPool(ZuulProperties.HystrixThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public boolean isSetContentLength() {
        return this.setContentLength;
    }

    public void setSetContentLength(boolean setContentLength) {
        this.setContentLength = setContentLength;
    }

    public boolean isIncludeDebugHeader() {
        return this.includeDebugHeader;
    }

    public void setIncludeDebugHeader(boolean includeDebugHeader) {
        this.includeDebugHeader = includeDebugHeader;
    }

    public int getInitialStreamBufferSize() {
        return this.initialStreamBufferSize;
    }

    public void setInitialStreamBufferSize(int initialStreamBufferSize) {
        this.initialStreamBufferSize = initialStreamBufferSize;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ZuulProperties that = (ZuulProperties)o;
            return this.addHostHeader == that.addHostHeader && this.addProxyHeaders == that.addProxyHeaders && this.forceOriginalQueryStringEncoding == that.forceOriginalQueryStringEncoding && Objects.equals(this.host, that.host) && Objects.equals(this.ignoredHeaders, that.ignoredHeaders) && Objects.equals(this.ignoredPatterns, that.ignoredPatterns) && Objects.equals(this.ignoredServices, that.ignoredServices) && this.ignoreLocalService == that.ignoreLocalService && this.ignoreSecurityHeaders == that.ignoreSecurityHeaders && Objects.equals(this.prefix, that.prefix) && this.removeSemicolonContent == that.removeSemicolonContent && Objects.equals(this.retryable, that.retryable) && Objects.equals(this.ribbonIsolationStrategy, that.ribbonIsolationStrategy) && Objects.equals(this.routes, that.routes) && Objects.equals(this.semaphore, that.semaphore) && Objects.equals(this.sensitiveHeaders, that.sensitiveHeaders) && Objects.equals(this.servletPath, that.servletPath) && this.sslHostnameValidationEnabled == that.sslHostnameValidationEnabled && this.stripPrefix == that.stripPrefix && this.setContentLength == that.setContentLength && this.includeDebugHeader == that.includeDebugHeader && this.initialStreamBufferSize == that.initialStreamBufferSize && Objects.equals(this.threadPool, that.threadPool) && this.traceRequestBody == that.traceRequestBody;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.addHostHeader, this.addProxyHeaders, this.forceOriginalQueryStringEncoding, this.host, this.ignoredHeaders, this.ignoredPatterns, this.ignoredServices, this.ignoreLocalService, this.ignoreSecurityHeaders, this.prefix, this.removeSemicolonContent, this.retryable, this.ribbonIsolationStrategy, this.routes, this.semaphore, this.sensitiveHeaders, this.servletPath, this.sslHostnameValidationEnabled, this.stripPrefix, this.threadPool, this.traceRequestBody, this.setContentLength, this.includeDebugHeader, this.initialStreamBufferSize});
    }

    public String toString() {
        return "ZuulProperties{" + "prefix='" + this.prefix + "', " + "stripPrefix=" + this.stripPrefix + ", " + "retryable=" + this.retryable + ", " + "routes=" + this.routes + ", " + "addProxyHeaders=" + this.addProxyHeaders + ", " + "addHostHeader=" + this.addHostHeader + ", " + "ignoredServices=" + this.ignoredServices + ", " + "ignoredPatterns=" + this.ignoredPatterns + ", " + "ignoredHeaders=" + this.ignoredHeaders + ", " + "ignoreSecurityHeaders=" + this.ignoreSecurityHeaders + ", " + "forceOriginalQueryStringEncoding=" + this.forceOriginalQueryStringEncoding + ", " + "servletPath='" + this.servletPath + "', " + "ignoreLocalService=" + this.ignoreLocalService + ", " + "host=" + this.host + ", " + "traceRequestBody=" + this.traceRequestBody + ", " + "removeSemicolonContent=" + this.removeSemicolonContent + ", " + "sensitiveHeaders=" + this.sensitiveHeaders + ", " + "sslHostnameValidationEnabled=" + this.sslHostnameValidationEnabled + ", " + "ribbonIsolationStrategy=" + this.ribbonIsolationStrategy + ", " + "semaphore=" + this.semaphore + ", " + "threadPool=" + this.threadPool + ", " + "setContentLength=" + this.setContentLength + ", " + "includeDebugHeader=" + this.includeDebugHeader + ", " + "initialStreamBufferSize=" + this.initialStreamBufferSize + ", " + "}";
    }

    public static class HystrixThreadPool {
        private boolean useSeparateThreadPools = false;
        private String threadPoolKeyPrefix = "";

        public HystrixThreadPool() {
        }

        public boolean isUseSeparateThreadPools() {
            return this.useSeparateThreadPools;
        }

        public void setUseSeparateThreadPools(boolean useSeparateThreadPools) {
            this.useSeparateThreadPools = useSeparateThreadPools;
        }

        public String getThreadPoolKeyPrefix() {
            return this.threadPoolKeyPrefix;
        }

        public void setThreadPoolKeyPrefix(String threadPoolKeyPrefix) {
            this.threadPoolKeyPrefix = threadPoolKeyPrefix;
        }
    }

    public static class HystrixSemaphore {
        private int maxSemaphores = 100;

        public HystrixSemaphore() {
        }

        public HystrixSemaphore(int maxSemaphores) {
            this.maxSemaphores = maxSemaphores;
        }

        public int getMaxSemaphores() {
            return this.maxSemaphores;
        }

        public void setMaxSemaphores(int maxSemaphores) {
            this.maxSemaphores = maxSemaphores;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                ZuulProperties.HystrixSemaphore that = (ZuulProperties.HystrixSemaphore)o;
                return this.maxSemaphores == that.maxSemaphores;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.maxSemaphores});
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("HystrixSemaphore{");
            sb.append("maxSemaphores=").append(this.maxSemaphores);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Host {
        private int maxTotalConnections = 200;
        private int maxPerRouteConnections = 20;
        private int socketTimeoutMillis = 10000;
        private int connectTimeoutMillis = 2000;
        private int connectionRequestTimeoutMillis = -1;
        private long timeToLive = -1L;
        private TimeUnit timeUnit;

        public Host() {
            this.timeUnit = TimeUnit.MILLISECONDS;
        }

        public Host(int maxTotalConnections, int maxPerRouteConnections, int socketTimeoutMillis, int connectTimeoutMillis, long timeToLive, TimeUnit timeUnit) {
            this.timeUnit = TimeUnit.MILLISECONDS;
            this.maxTotalConnections = maxTotalConnections;
            this.maxPerRouteConnections = maxPerRouteConnections;
            this.socketTimeoutMillis = socketTimeoutMillis;
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.timeToLive = timeToLive;
            this.timeUnit = timeUnit;
        }

        public int getMaxTotalConnections() {
            return this.maxTotalConnections;
        }

        public void setMaxTotalConnections(int maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
        }

        public int getMaxPerRouteConnections() {
            return this.maxPerRouteConnections;
        }

        public void setMaxPerRouteConnections(int maxPerRouteConnections) {
            this.maxPerRouteConnections = maxPerRouteConnections;
        }

        public int getSocketTimeoutMillis() {
            return this.socketTimeoutMillis;
        }

        public void setSocketTimeoutMillis(int socketTimeoutMillis) {
            this.socketTimeoutMillis = socketTimeoutMillis;
        }

        public int getConnectTimeoutMillis() {
            return this.connectTimeoutMillis;
        }

        public void setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
        }

        public int getConnectionRequestTimeoutMillis() {
            return this.connectionRequestTimeoutMillis;
        }

        public void setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
            this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
        }

        public long getTimeToLive() {
            return this.timeToLive;
        }

        public void setTimeToLive(long timeToLive) {
            this.timeToLive = timeToLive;
        }

        public TimeUnit getTimeUnit() {
            return this.timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                ZuulProperties.Host host = (ZuulProperties.Host)o;
                return this.maxTotalConnections == host.maxTotalConnections && this.maxPerRouteConnections == host.maxPerRouteConnections && this.socketTimeoutMillis == host.socketTimeoutMillis && this.connectTimeoutMillis == host.connectTimeoutMillis && this.connectionRequestTimeoutMillis == host.connectionRequestTimeoutMillis && this.timeToLive == host.timeToLive && this.timeUnit == host.timeUnit;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.maxTotalConnections, this.maxPerRouteConnections, this.socketTimeoutMillis, this.connectTimeoutMillis, this.connectionRequestTimeoutMillis, this.timeToLive, this.timeUnit});
        }

        public String toString() {
            return (new ToStringCreator(this)).append("maxTotalConnections", this.maxTotalConnections).append("maxPerRouteConnections", this.maxPerRouteConnections).append("socketTimeoutMillis", this.socketTimeoutMillis).append("connectTimeoutMillis", this.connectTimeoutMillis).append("connectionRequestTimeoutMillis", this.connectionRequestTimeoutMillis).append("timeToLive", this.timeToLive).append("timeUnit", this.timeUnit).toString();
        }
    }

    public static class ZuulRoute {
        private String id;
        private String path;
        private String serviceId;
        private String url;
        private boolean stripPrefix = true;
        private Boolean retryable;
        private Set<String> sensitiveHeaders = new LinkedHashSet();
        private boolean customSensitiveHeaders = false;

        public ZuulRoute() {
        }

        public ZuulRoute(String id, String path, String serviceId, String url, boolean stripPrefix, Boolean retryable, Set<String> sensitiveHeaders) {
            this.id = id;
            this.path = path;
            this.serviceId = serviceId;
            this.url = url;
            this.stripPrefix = stripPrefix;
            this.retryable = retryable;
            this.sensitiveHeaders = sensitiveHeaders;
            this.customSensitiveHeaders = sensitiveHeaders != null;
        }

        public ZuulRoute(String text) {
            String location = null;
            String path = text;
            if (text.contains("=")) {
                String[] values = StringUtils.trimArrayElements(StringUtils.split(text, "="));
                location = values[1];
                path = values[0];
            }

            this.id = this.extractId(path);
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            this.setLocation(location);
            this.path = path;
        }

        public ZuulRoute(String path, String location) {
            this.id = this.extractId(path);
            this.path = path;
            this.setLocation(location);
        }

        public String getLocation() {
            return StringUtils.hasText(this.url) ? this.url : this.serviceId;
        }

        public void setLocation(String location) {
            if (location == null || !location.startsWith("http:") && !location.startsWith("https:")) {
                this.serviceId = location;
            } else {
                this.url = location;
            }

        }

        private String extractId(String path) {
            path = path.startsWith("/") ? path.substring(1) : path;
            path = path.replace("/*", "").replace("*", "");
            return path;
        }

        public Route getRoute(String prefix) {
            return new Route(this.id, this.path, this.getLocation(), prefix, this.retryable, this.isCustomSensitiveHeaders() ? this.sensitiveHeaders : null, this.stripPrefix);
        }

        public boolean isCustomSensitiveHeaders() {
            return this.customSensitiveHeaders;
        }

        public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
            this.customSensitiveHeaders = customSensitiveHeaders;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return this.path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return this.serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isStripPrefix() {
            return this.stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return this.retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public Set<String> getSensitiveHeaders() {
            return this.sensitiveHeaders;
        }

        public void setSensitiveHeaders(Set<String> headers) {
            this.customSensitiveHeaders = true;
            this.sensitiveHeaders = new LinkedHashSet(headers);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                ZuulProperties.ZuulRoute that = (ZuulProperties.ZuulRoute)o;
                return this.customSensitiveHeaders == that.customSensitiveHeaders && Objects.equals(this.id, that.id) && Objects.equals(this.path, that.path) && Objects.equals(this.retryable, that.retryable) && Objects.equals(this.sensitiveHeaders, that.sensitiveHeaders) && Objects.equals(this.serviceId, that.serviceId) && this.stripPrefix == that.stripPrefix && Objects.equals(this.url, that.url);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.customSensitiveHeaders, this.id, this.path, this.retryable, this.sensitiveHeaders, this.serviceId, this.stripPrefix, this.url});
        }

        public String toString() {
            return "ZuulRoute{" + "id='" + this.id + "', " + "path='" + this.path + "', " + "serviceId='" + this.serviceId + "', " + "url='" + this.url + "', " + "stripPrefix=" + this.stripPrefix + ", " + "retryable=" + this.retryable + ", " + "sensitiveHeaders=" + this.sensitiveHeaders + ", " + "customSensitiveHeaders=" + this.customSensitiveHeaders + ", " + "}";
        }
    }
}
