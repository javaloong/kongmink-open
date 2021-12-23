package org.javaloong.kongmink.open.am.embedded.keycloak.support;

import org.keycloak.platform.PlatformProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SimplePlatformProvider implements PlatformProvider {

    private static final Logger log = LoggerFactory.getLogger(SimplePlatformProvider.class);

    private File tmpDir;

    @Override
    public void onStartup(Runnable startupHook) {
        startupHook.run();
    }

    @Override
    public void onShutdown(Runnable shutdownHook) {
    }

    @Override
    public void exit(Throwable cause) {
        throw new RuntimeException(cause);
    }

    @Override
    public File getTmpDirectory() {
        if (tmpDir == null) {
            String projectBuildDir = System.getProperty("project.build.directory");
            File tmpDir;
            if (projectBuildDir != null) {
                tmpDir = new File(projectBuildDir, "server-tmp");
                tmpDir.mkdir();
            } else {
                try {
                    tmpDir = Files.createTempDirectory("keycloak-server-").toFile();
                    tmpDir.deleteOnExit();
                } catch (IOException ioe) {
                    throw new RuntimeException("Could not create temporary directory", ioe);
                }
            }

            if (tmpDir.isDirectory()) {
                this.tmpDir = tmpDir;
                log.info("Using server tmp directory: %s", tmpDir.getAbsolutePath());
            } else {
                throw new RuntimeException("Directory " + tmpDir + " was not created and does not exists");
            }
        }
        return tmpDir;
    }
}
