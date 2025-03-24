package com.root.sms.vo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpaceVO {
    private Long pid;
    private Long societyId;
    private String parkingId;
}
