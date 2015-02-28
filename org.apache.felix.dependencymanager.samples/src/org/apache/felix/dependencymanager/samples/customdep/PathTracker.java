package org.apache.felix.dependencymanager.samples.customdep;

import org.osgi.service.log.LogService;

public class PathTracker {
    volatile LogService logService;
    
    void start() {
        logService.log(LogService.LOG_INFO, "PathTracker.start");
    }
    
    void stop() {
        logService.log(LogService.LOG_INFO, "PathTracker.stop");
    }

    void add(String path) {
        logService.log(LogService.LOG_INFO, "PathTracker.add: " + path);
    }
    
    void change(String path) {
        logService.log(LogService.LOG_INFO, "PathTracker.change: " + path);
    }

    void remove(String path) {
        logService.log(LogService.LOG_INFO, "PathTracker.remove: " + path);
    }
}
