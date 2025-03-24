package com.root.sms.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
