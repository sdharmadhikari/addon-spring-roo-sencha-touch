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
     * @param controller the JavaType of the controller under test (required)
     * @param name the name of the test case (optional)
     * @param serverURL the URL of the Selenium server (optional)
     */
    void generateSenchaTouchCode(JavaType controller, String name, String serverURL);

    boolean isSenchaTouchInstallationPossible();
}