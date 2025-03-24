package com.root.sms.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomVO {
    private Long societyId;
    private Long rid;
    private String roomNo;
    private String roomSize;
}
