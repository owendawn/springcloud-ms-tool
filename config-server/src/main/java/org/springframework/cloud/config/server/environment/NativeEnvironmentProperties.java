package org.springframework.cloud.config.server.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.support.EnvironmentRepositoryProperties;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@Component
@ConfigurationProperties("spring.cloud.config.server.native")
public class NativeEnvironmentProperties implements EnvironmentRepositoryProperties {

    /**
     * Flag to determine how to handle exceptions during decryption (default false).
     */
    private Boolean failOnError = false;

    /**
     * Flag to determine whether label locations should be added.
     */
    private Boolean addLabelLocations = true;

    private String defaultLabel = "master";

    /**
     * Locations to search for configuration files. Defaults to the same as a Spring Boot
     * app so [classpath:/,classpath:/config/,file:./,file:./config/].
     */
    private String[] searchLocations = new String[0];

    /**
     * Version string to be reported for native repository.
     */
    private String version;

    private int order = Ordered.LOWEST_PRECEDENCE;

    public Boolean getFailOnError() {
        return this.failOnError;
    }

    public void setFailOnError(Boolean failOnError) {
        this.failOnError = failOnError;
    }

    public Boolean getAddLabelLocations() {
        return this.addLabelLocations;
    }

    public void setAddLabelLocations(Boolean addLabelLocations) {
        this.addLabelLocations = addLabelLocations;
    }

    public String getDefaultLabel() {
        return this.defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public String[] getSearchLocations() {
        return this.searchLocations;
    }

    public void setSearchLocations(String[] searchLocations) {
        List<String> locations = new ArrayList<>();
        String modulePath = null;
        try {
            modulePath = new File("").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String location : searchLocations) {
            if (location.contains("*")) {
                Stream.of(getPathByIndistinct(location)).forEach(it -> {
                    locations.add(it);
                });
            } else {
                locations.add(location);
            }
        }
        this.searchLocations = locations.toArray(new String[]{});
    }

    private String[] getPathByIndistinct(String url) {
        url = url.replaceAll("\\\\", "/");
        int idx = url.indexOf("/*/");
        if (idx == -1 && url.endsWith("/*")) {
            idx = url.indexOf("/*");
        }
        File root = new File(url.substring(0, idx));
        if (url.contains("file:")) {
            root = new File(url.substring(0, idx).replace("file:///", ""));
        }
        if (root.exists()) {
            System.out.println(Arrays.toString(root.list()));
            String finalUrl = url;
            int finalIdx = idx;
            return Stream.of(root.list())
                    .map(it -> finalUrl.substring(0, finalIdx) + "/" + it + finalUrl.substring(finalIdx + 2))
                    .toArray(String[]::new);
        }
        return new String[]{};
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

}
