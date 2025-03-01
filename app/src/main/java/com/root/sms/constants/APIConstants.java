package com.root.sms.constants;

public final class APIConstants {

    public static final String domain = "http://192.168.29.148:";
    private static final String portNo = "8085";
    private static final String fileApiPortNo = "8090";
    private static final String domainSuffix = "/api";
    private static final String finalDomain = domain + portNo + domainSuffix;
    private static final String fileApiDomain = domain + fileApiPortNo;

    public static final String loginPostApi = finalDomain + "/user/login";
    public static final int loginPostApiRequestId = 1001;

    public static final String fileUploadApi = fileApiDomain + "/sms/files/upload";
    public static final int fileUploadApiRequestId = 1002;

    public static final String profileUploadApi = fileApiDomain + "/sms/files/upload-profile";
    public static final int profileUploadApiRequestId = 1003;

    public static final String addSocietyApi = finalDomain + "/sms/societies/register";
    public static final int addSocietyApiRequestId = 1004;

    public static final String addRoomsApi = finalDomain + "/sms/room/addRooms";
    public static final int addRoomsApiRequestId = 1005;

}
