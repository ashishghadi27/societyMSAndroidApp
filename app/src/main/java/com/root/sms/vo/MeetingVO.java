package com.root.sms.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingVO {

    private String title;
    private String agenda;
    private String startDateTime;
    private String endDateTime;
    private Long sid;

}
