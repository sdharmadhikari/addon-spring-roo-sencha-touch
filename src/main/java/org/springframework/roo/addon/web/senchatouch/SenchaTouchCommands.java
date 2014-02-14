package org.springframework.roo.addon.web.senchatouch;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Commands for the 'senchatouch' add-on to be used by the ROO shell.
 * 
 * @author Stefan Schmidt
 * @since 1.0
 */
@Component
@Service
public class SenchaTouchCommands implements CommandMarker {

    @Reference private SenchaTouchOperations senchaTouchOperations;

    @CliCommand(value = "senchatouch generate", help = "Creates a new Sencha Touch based app for a particular controller")
    public void generateTest(
            @CliOption(key = "controller", mandatory = true, help = "Controller to create a Selenium test for") final JavaType controller,
            @CliOption(key = "name", mandatory = false, help = "Name of the test") final String name,
            @CliOption(key = "serverUrl", mandatory = false, unspecifiedDefaultValue = "http://localhost:8080/", specifiedDefaultValue = "http://localhost:8080/", help = "URL of the server where the web application is available, including protocol, port and hostname") final String url) {

        senchaTouchOperations.generateSenchaTouchCode(controller, name, url);
    }

    @CliAvailabilityIndicator({ "senchatouch generate" })
    public boolean isJdkFieldManagementAvailable() {
        return senchaTouchOperations.isSenchaTouchInstallationPossible();
    }
}