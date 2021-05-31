package com.dutch.IngatmInfo.service;


import com.dutch.IngatmInfo.controller.IngAtmInfoController;
import com.dutch.IngatmInfo.model.Address;
import com.dutch.IngatmInfo.model.Hours;
import com.dutch.IngatmInfo.model.INGAtmsInfo;
import com.dutch.IngatmInfo.model.OpeningHours;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.IntStream;

import static com.dutch.IngatmInfo.constants.IngAtmInfoConstants.*;

@Service
public class IngAtmInfoService {

    Logger logger = LogManager.getLogger(IngAtmInfoService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dutchCitysAtmInfo}")
    private String dutchCitysAtmInfoURI;

    public List<INGAtmsInfo> getAtmInfoByCity(String city) {
        logger.info("IngAtmInfoService -  getAtmInfoByCity - START");
        List<INGAtmsInfo> iNGAtmsInfoList = new ArrayList<>();
        ResponseEntity<String> response = null;
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        try {
            response = restTemplate.exchange(dutchCitysAtmInfoURI, HttpMethod.GET, entity, new ParameterizedTypeReference<String>() {
            });
            StringBuffer jsonFormat = new StringBuffer(response.getBody());
            jsonFormat.delete(0, 5);
            JSONArray jsonArray = new JSONArray(jsonFormat.toString().trim());
            IntStream.rangeClosed(0, jsonArray.length() - 1).forEach(index -> {
                JSONObject object = (JSONObject) jsonArray.get(index);
                JSONObject addressJson = (JSONObject) object.get(ADDRESS);
                String cityName = (String) addressJson.get(CITY);
                if (cityName.equalsIgnoreCase(city)) {
                    INGAtmsInfo iNGAtmsInfo = generateINGAtmsInfoDto(object, addressJson);
                    iNGAtmsInfoList.add(iNGAtmsInfo);
                }
            });
            logger.info("IngAtmInfoService -  getAtmInfoByCity - size of records for given city  : " + iNGAtmsInfoList.size());
            return iNGAtmsInfoList;
        } catch (Exception e) {
            logger.error("IngAtmInfoService -  getAtmInfoByCity - ERROR : " + e.getMessage());
        }
        logger.info("IngAtmInfoService -  getAtmInfoByCity - END");
        logger.info("IngAtmInfoService -  getAtmInfoByCity - No records found for given city  : " + iNGAtmsInfoList.size());
        return iNGAtmsInfoList;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    INGAtmsInfo generateINGAtmsInfoDto(JSONObject object, JSONObject addressJson) {
        INGAtmsInfo iNGAtmsInfo = new INGAtmsInfo();
        iNGAtmsInfo.setCity((String) addressJson.get(CITY));
        Address address = new Address();
        address.setStreet((String) addressJson.get(STREET));
        address.setHouseNumber((String) addressJson.get(HOUSE_NUMBER));
        address.setPostalCode((String) addressJson.get(POSTAL_CODE));
        address.setCity((String) addressJson.get(CITY));
        iNGAtmsInfo.setAddress(address);
        iNGAtmsInfo.setDistance(object.get(DISTANCE).toString());
        iNGAtmsInfo.setFunctionality(object.get(FUNCTIONALITY).toString());
        iNGAtmsInfo.setType(object.get(TYPE).toString());
        JSONArray openingHours = (JSONArray) object.get(OPENING_HOURS);
        List<OpeningHours> openingHoursList = new ArrayList<>();
        IntStream.rangeClosed(0, openingHours.length() - 1).forEach(size -> {
            OpeningHours openingHoursOutput = new OpeningHours();
            JSONObject hrs = (JSONObject) openingHours.get(size);
            openingHoursOutput.setDayOfWeek(hrs.get(DAY_OF_WEEK).toString());
            JSONArray hoursJson = (JSONArray) hrs.get(HOURS);
            List<Hours> hoursList = new ArrayList<>();
            IntStream.rangeClosed(0, hoursJson.length() - 1).forEach(i -> {
                JSONObject hourJson = (JSONObject) hoursJson.get(i);
                Hours hour = new Hours();
                hour.setHoursFrom(hourJson.get(HOUR_FROM).toString());
                hour.setHoursTo(hourJson.get(HOUR_TO).toString());
                hoursList.add(hour);
                openingHoursOutput.setHours(hoursList);
            });
            openingHoursList.add(openingHoursOutput);
        });
        iNGAtmsInfo.setOpeningHoursList(openingHoursList);

        return iNGAtmsInfo;
    }
}



