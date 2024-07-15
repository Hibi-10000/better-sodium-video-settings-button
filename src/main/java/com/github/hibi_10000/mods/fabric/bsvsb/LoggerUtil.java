package com.github.hibi_10000.mods.fabric.bsvsb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private LoggerUtil() {}
    private static final Logger logger = LoggerFactory.getLogger("bsvsb");

    public static void throwError(Throwable t) {
        logger.error("[BSVSB] exception occurred", t);
    }
}
