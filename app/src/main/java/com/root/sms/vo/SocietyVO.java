package com.root.sms.vo;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocietyVO {
    private Long sid;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String plotNumber;
    private String profilePic;
    private String societyFund;
    private Boolean parkingAvailable;
    private Boolean isApproved;
    private List<SocietyFileVO> files;
}
