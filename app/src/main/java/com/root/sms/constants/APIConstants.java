package com.root.sms.constants;

public final class APIConstants {

    public static final String domain = "http://192.168.29.118:";
    private static final String portNo = "2798";
    private static final String domainSuffix = "/Story";
    private static final String finalDomain = domain + portNo + domainSuffix;

    public static final String loginPostApi = finalDomain + "/user/login";
    public static final int loginPostApiRequestId = 1001;
}
