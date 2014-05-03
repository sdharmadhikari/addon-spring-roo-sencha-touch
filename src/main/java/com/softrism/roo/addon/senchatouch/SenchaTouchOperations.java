package com.softrism.roo.addon.senchatouch;

import org.springframework.roo.model.JavaType;

/**
 * Provides Selenium operations.
 * 
 * @author Ben Alex
 * @since 1.0
 */
public interface SenchaTouchOperations {

    /**
     * Creates a new Selenium testcase
     *
     * @param serverURL the URL of the Selenium server (optional)
     */
    void generateSenchaTouchCode(String serverURL);

    boolean isSenchaTouchInstallationPossible();
}