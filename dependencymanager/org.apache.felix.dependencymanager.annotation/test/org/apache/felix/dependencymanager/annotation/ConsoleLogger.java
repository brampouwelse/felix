package org.apache.felix.dependencymanager.annotation;

import org.apache.felix.dm.annotation.plugin.bnd.Logger;

public class ConsoleLogger extends Logger {

    @Override
    public void warn(String msg, Object... args) {
        System.out.println(String.format("[WARN] " + msg, args));
    }

    @Override
    public void info(String msg, Object... args) {
        System.out.println(String.format("[INFO] " + msg, args));
    }

    @Override
    public void error(String msg, Throwable err, Object... args) {
        System.out.println(String.format("[ERROR] " + msg, args));
        err.printStackTrace();
    }

    @Override
    public void error(String msg, Object... args) {
        System.out.println(String.format("[ERROR] " + msg, args));

    }

    @Override
    public void debug(String msg, Object... args) {
        System.out.println(String.format("[DEBUG] " + msg, args));
    }
}