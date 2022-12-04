package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.service.IStatisticService;
import com.datn.coworkingspace.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticAPI {

    @Autowired
    private IStatisticService statisticService;

    @GetMapping("/revenue")
    public ResponseEntity<?> getAllRevenue(@RequestParam(name = "day", required = false) String day,
                                           @RequestParam(name = "month", required = false) String month,
                                           @RequestParam(name = "quarter", required = false) String quarter,
                                           @RequestParam(name = "year", required = false) String year) throws ParseException {
        if(day != null) {
            return new ResponseEntity<>(statisticService.getAllRevenueByDay(day), HttpStatus.OK);
        }
        if(month != null) {
            return new ResponseEntity<>(statisticService.getAllRevenueByMonth(month), HttpStatus.OK);
        }
        if(quarter != null) {
            return new ResponseEntity<>(statisticService.getAllRevenueByQuarter(quarter), HttpStatus.OK);
        }
        if(year != null) {
            return new ResponseEntity<>(statisticService.getAllRevenueByYear(year), HttpStatus.OK);
        }

        if(day == null && month == null && quarter == null && year == null) {
            return new ResponseEntity<>(statisticService.getAllRevenueByMonth(CommonUtils.covertDateNowToMonthString()), HttpStatus.OK);
        }

        return new ResponseEntity<>(new MessageResponse("Please provide time to get revenue", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
