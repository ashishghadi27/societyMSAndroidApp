package com.root.sms.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private Long roomId;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;
    private String type;
}
