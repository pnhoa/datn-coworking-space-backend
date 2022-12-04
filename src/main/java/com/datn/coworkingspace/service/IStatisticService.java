package com.datn.coworkingspace.service;

import java.math.BigDecimal;
import java.text.ParseException;


public interface IStatisticService {

    BigDecimal getAllRevenueByDay(String day) throws ParseException;
    BigDecimal getAllRevenueByMonth(String month);
    BigDecimal getAllRevenueByQuarter(String quarter);
    BigDecimal getAllRevenueByYear(String year);

}
