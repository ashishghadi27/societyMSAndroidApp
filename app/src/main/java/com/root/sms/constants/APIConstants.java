package com.root.sms.constants;

public final class APIConstants {

    private static final String domain = "http://192.168.29.148:";
    private static final String portNo = "8085";
    private static final String fileApiPortNo = "8090";
    private static final String domainSuffix = "/api";
    private static final String finalDomain = domain + portNo + domainSuffix;
    private static final String fileApiDomain = domain + fileApiPortNo;

    public static final String loginPostApi = finalDomain + "/auth-service/auth/user/login";
    public static final int loginPostApiRequestId = 1001;

    public static final String fileUploadApi = fileApiDomain + "/sms/files/upload";
    public static final int fileUploadApiRequestId = 1002;

    public static final String profileUploadApi = fileApiDomain + "/sms/files/upload-profile";
    public static final int profileUploadApiRequestId = 1003;

    public static final String addSocietyApi = finalDomain + "/society-mgmt-service/sms/societies/register";
    public static final int addSocietyApiRequestId = 1004;

    public static final String addRoomsApi = finalDomain + "/society-mgmt-service/sms/room/addRooms";
    public static final int addRoomsApiRequestId = 1005;

    public static final String getRoomsApi = finalDomain + "/society-mgmt-service/sms/room/getRooms?societyId=%s";
    public static final int getRoomsApiRequestId = 1006;

    public static final String registerMemberApi = finalDomain + "/society-mgmt-service/sms/member/addMember";
    public static final int registerMemberApiRequestId = 1007;

    public static final String getSocietyApi = finalDomain + "/society-mgmt-service/sms/societies/get?id=%s";
    public static final int getSocietyApiRequestId = 1008;
}
