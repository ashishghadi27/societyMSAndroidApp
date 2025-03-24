package com.root.sms.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class MemberVO {
    private Long mid;
    private Long roomId;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;
    private String type;
}
