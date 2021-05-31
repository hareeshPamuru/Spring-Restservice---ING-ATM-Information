package com.dutch.IngatmInfo.controller;

import com.dutch.IngatmInfo.model.INGAtmsInfo;
import com.dutch.IngatmInfo.service.IngAtmInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dutch.IngatmInfo.constants.IngAtmInfoConstants.*;


@RestController
@RequestMapping(path = "/ING")
public class IngAtmInfoController
{
   Logger logger= LogManager.getLogger(IngAtmInfoController.class);

    @Autowired
    private IngAtmInfoService ingAtmInfoService;
    
    @GetMapping(path="/getAtmInfoByCity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAtmInfoByCity(@RequestParam(value = CITY) String city)
    {
        logger.info("IngAtmInfoController -getAtmInfoByCity - START ");
        List<INGAtmsInfo> ingAtmsInfoList=null;
        try {
            if (city != null && !city.isEmpty() && !city.trim().isEmpty()){
                ingAtmsInfoList =ingAtmInfoService.getAtmInfoByCity(city);
                if(ingAtmsInfoList.size()>0){

                    return new ResponseEntity<Object>(ingAtmsInfoList, HttpStatus.OK);
                }
                else{
                    logger.debug("IngAtmInfoController -getAtmInfoByCity - " + NO_RECORD_MATCHED);
                    return new ResponseEntity<Object>(NO_RECORD_MATCHED, HttpStatus.BAD_REQUEST);
                }
            }else{
                logger.debug("IngAtmInfoController -getAtmInfoByCity - " + CITY_EMPTY_ERROR);
                return new ResponseEntity<Object>(CITY_EMPTY_ERROR, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            logger.error("IngAtmInfoController -getAtmInfoByCity - " + e.getMessage());
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
