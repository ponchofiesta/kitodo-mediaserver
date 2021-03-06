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

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties for the importer.
 */
@Configuration
@ConfigurationProperties(prefix = "importer")
public class ImporterProperties {

    private String hotfolderPath;
    private String importingFolderPath;
    private String tempWorkFolderPath;
    private String errorFolderPath;
    private String workFilesPath;
    private String workIdRegex;
    private String workDataReaderXsl;
    private String fileUrlReaderXsl;
    private String cron;
    private boolean indexWorkAfterImport;
    private List<String> validationFileGrps;
    private List<String> errorNotificationEmail;
    private List<String> reportNotificationEmail;
    private List<Map<String, Map<String, String>>> actionsBeforeIndexing;
    private List<Map<String, Map<String, String>>> actionsAfterSuccessfulIndexing;
    private List<Map<String, Map<String, String>>> actionsToRequestAsynchronously;

    public String getHotfolderPath() {
        return hotfolderPath;
    }

    public void setHotfolderPath(String hotfolderPath) {
        this.hotfolderPath = hotfolderPath;
    }

    public String getImportingFolderPath() {
        return importingFolderPath;
    }

    public void setImportingFolderPath(String importingFolderPath) {
        this.importingFolderPath = importingFolderPath;
    }

    public String getTempWorkFolderPath() {
        return tempWorkFolderPath;
    }

    public void setTempWorkFolderPath(String tempWorkFolderPath) {
        this.tempWorkFolderPath = tempWorkFolderPath;
    }

    public String getErrorFolderPath() {
        return errorFolderPath;
    }

    public void setErrorFolderPath(String errorFolderPath) {
        this.errorFolderPath = errorFolderPath;
    }

    public String getWorkFilesPath() {
        return workFilesPath;
    }

    public void setWorkFilesPath(String workFilesPath) {
        this.workFilesPath = workFilesPath;
    }

    public String getWorkIdRegex() {
        return workIdRegex;
    }

    public void setWorkIdRegex(String workIdRegex) {
        this.workIdRegex = workIdRegex;
    }

    public String getWorkDataReaderXsl() {
        return workDataReaderXsl;
    }

    public void setWorkDataReaderXsl(String workDataReaderXsl) {
        this.workDataReaderXsl = workDataReaderXsl;
    }

    public String getFileUrlReaderXsl() {
        return fileUrlReaderXsl;
    }

    public void setFileUrlReaderXsl(String fileUrlReaderXsl) {
        this.fileUrlReaderXsl = fileUrlReaderXsl;
    }

    public List<String> getValidationFileGrps() {
        return validationFileGrps;
    }

    public void setValidationFileGrps(List<String> validationFileGrps) {
        this.validationFileGrps = validationFileGrps;
    }

    public List<String> getErrorNotificationEmail() {
        return errorNotificationEmail;
    }

    public void setErrorNotificationEmail(List<String> errorNotificationEmail) {
        this.errorNotificationEmail = errorNotificationEmail;
    }

    public List<String> getReportNotificationEmail() {
        return reportNotificationEmail;
    }

    public void setReportNotificationEmail(List<String> reportNotificationEmail) {
        this.reportNotificationEmail = reportNotificationEmail;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public boolean isIndexWorkAfterImport() {
        return indexWorkAfterImport;
    }

    public void setIndexWorkAfterImport(boolean indexWorkAfterImport) {
        this.indexWorkAfterImport = indexWorkAfterImport;
    }

    public List<Map<String, Map<String, String>>> getActionsBeforeIndexing() {
        return actionsBeforeIndexing;
    }

    public void setActionsBeforeIndexing(List<Map<String, Map<String, String>>> actionsBeforeIndexing) {
        this.actionsBeforeIndexing = actionsBeforeIndexing;
    }

    public List<Map<String, Map<String, String>>> getActionsAfterSuccessfulIndexing() {
        return actionsAfterSuccessfulIndexing;
    }

    public void setActionsAfterSuccessfulIndexing(List<Map<String, Map<String, String>>> actionsAfterSuccessfulIndexing) {
        this.actionsAfterSuccessfulIndexing = actionsAfterSuccessfulIndexing;
    }

    public List<Map<String, Map<String, String>>> getActionsToRequestAsynchronously() {
        return actionsToRequestAsynchronously;
    }

    public void setActionsToRequestAsynchronously(List<Map<String, Map<String, String>>> actionsToRequestAsynchronously) {
        this.actionsToRequestAsynchronously = actionsToRequestAsynchronously;
    }
}
