package com.project.restfull.api.utils;

public class Utils {
    public static Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 24 * 30);
    }
}
