package com.dutch.IngatmInfo.model;

import lombok.Data;

import java.util.List;

@Data
public class INGAtmsInfo {
    private String city;
    private Address address;
    private String distance;
    private String functionality;
    private String type;
    private List<OpeningHours> openingHoursList;

}
