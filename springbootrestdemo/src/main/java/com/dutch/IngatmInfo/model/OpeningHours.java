package com.dutch.IngatmInfo.model;

import lombok.Data;

import java.util.List;

@Data
public class OpeningHours {
    private List<Hours> hours;
    private String dayOfWeek;
}
