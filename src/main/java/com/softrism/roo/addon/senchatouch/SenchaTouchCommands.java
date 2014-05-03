package com.softrism.roo.addon.senchatouch;

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

    @CliCommand(value = "senchatouch generate app", help = "Creates a new Sencha Touch based app for the roo app")
    public void generateSenchaCode(
            @CliOption(key = "serverUrl", mandatory = false, unspecifiedDefaultValue = "http://localhost:8080/", specifiedDefaultValue = "http://localhost:8080/", help = "URL of the server where the server web application is available, including protocol, port and hostname") final String url) {

        senchaTouchOperations.generateSenchaTouchCode( url);
    }

    @CliAvailabilityIndicator({ "senchatouch generate" })
    public boolean isJdkFieldManagementAvailable() {
        return senchaTouchOperations.isSenchaTouchInstallationPossible();
    }
}