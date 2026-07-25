package org.gsdistance.grimmsServer.Commands.GUtilCommand;

import org.bukkit.command.CommandSender;
import org.gsdistance.grimmsServer.GrimmsServer;

import java.io.InputStream;

public class Version {

    private static String cachedBuildVersion = null;

    private static String getBuildVersion() {
        if (cachedBuildVersion == null) {
            try (InputStream is = GrimmsServer.instance.getResource("build.v")) {
                if (is != null) {
                    cachedBuildVersion = new String(is.readAllBytes()).trim();
                } else {
                    cachedBuildVersion = "unknown";
                }
            } catch (Exception e) {
                cachedBuildVersion = "error";
            }
        }
        return cachedBuildVersion;
    }

    public static boolean subCommand(CommandSender sender, String[] args) {
        String name = GrimmsServer.instance.getDescription().getPrefix();
        sender.sendMessage(name + " v" + GrimmsServer.instance.getDescription().getVersion() + " (build " + getBuildVersion() + ")");
        return true;
    }
}
