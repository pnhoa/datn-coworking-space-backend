package com.datn.coworkingspace.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface IStatisticService {

    BigDecimal getAllRevenueByDay(String day) throws ParseException;
    BigDecimal getAllRevenueByMonth(String month);
    BigDecimal getAllRevenueByQuarter(String quarter);
    BigDecimal getAllRevenueByYear(String year);

    List<Map<String, Object>> getTotalSpaceBookingGroupByCategoryByMonthInYear1(String year);

    Long getTotalSpaceActive();

    Long getTotalBooking();

    List<Map<String, Object>> getAllTopSpaceByDay(String day);
    List<Map<String, Object>> getAllTopSpaceByMonth(String month);
    List<Map<String, Object>> getAllTopSpaceByQuarter(String quarter);
    List<Map<String, Object>> getAllTopSpaceByYear(String year);
}
