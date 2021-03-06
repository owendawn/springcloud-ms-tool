/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.turbine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import util.DomXmlUtils;

/**
 * @author Spencer Gibb
 * @author Gregor Zurowski
 */
@Component
@ConfigurationProperties("turbine")
public class TurbineProperties {

	private String clusterNameExpression;

	private String appConfig;

	private boolean combineHostPort = true;

	public List<String> getAppConfigList() {
		if (!StringUtils.hasText(this.appConfig)) {
			return null;
		}
		String[] parts = StringUtils.commaDelimitedListToStringArray(this.appConfig);
		if (parts != null && parts.length > 0) {
			parts = StringUtils.trimArrayElements(parts);
			return Arrays.asList(parts);
		}
		return null;
	}

	public String getClusterNameExpression() {
		return clusterNameExpression;
	}

	public void setClusterNameExpression(String clusterNameExpression) {
		this.clusterNameExpression = clusterNameExpression;
	}

	public String getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(String appConfig) {
		this.appConfig = getAppConfigsFromXml();
	}

	private String getAppConfigsFromXml() {
		String modulePath = null;
		try {
			modulePath = new File("").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DomXmlUtils domXmlUtils = new DomXmlUtils(new File(modulePath + "/appConfigs.xml"));
		return domXmlUtils.parseXml(new DomXmlUtils.BaseDomXmlParser<String>() {
			@Override
			protected String parse(Document doc) {
				Element root = doc.getDocumentElement();
				NodeList routes = root.getElementsByTagName("app-config");
				StringBuilder stringBuilder=new StringBuilder();
				for (int i = 0; i < routes.getLength(); i++) {
					Element route = (Element) routes.item(i);
					if(i>0){
						stringBuilder.append(",");
					}
					stringBuilder.append(route.getAttribute("serviceId"));
				}
				return stringBuilder.toString();
			}
		});
	}

	public boolean isCombineHostPort() {
		return combineHostPort;
	}

	public void setCombineHostPort(boolean combineHostPort) {
		this.combineHostPort = combineHostPort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TurbineProperties that = (TurbineProperties) o;
		return Objects.equals(clusterNameExpression, that.clusterNameExpression)
				&& Objects.equals(appConfig, that.appConfig)
				&& Objects.equals(combineHostPort, that.combineHostPort);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clusterNameExpression, appConfig, combineHostPort);
	}

	@Override
	public String toString() {
		return new StringBuilder("TurbineProperties{").append("clusterNameExpression='")
				.append(clusterNameExpression).append("', ").append("appConfig='")
				.append(appConfig).append("', ").append("combineHostPort=")
				.append(combineHostPort).append("}").toString();
	}

}
