package com.root.sms.vo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomVO {
    private Long societyId;
    private Long rid;
    private String roomNo;
    private String roomSize;
}
