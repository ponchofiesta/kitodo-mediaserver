/*
 * (c) Kitodo. Key to digital objects e. V. <contact@kitodo.org>
 *
 * This file is part of the Kitodo project.
 *
 * It is licensed under GNU General Public License version 3 or later.
 *
 * For the full copyright and license information, please read the
 * LICENSE file that was distributed with this source code.
 */

package org.kitodo.mediaserver.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Application properties.
 */
@ControllerAdvice // make model attributes visible to MVC
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String version;
    private String timestamp;

    @ModelAttribute("applicationVersion")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @ModelAttribute("applicationTimestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
