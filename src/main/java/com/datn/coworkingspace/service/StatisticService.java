package com.datn.coworkingspace.service;

import com.datn.coworkingspace.entity.Category;
import com.datn.coworkingspace.enums.BookingStatus;
import com.datn.coworkingspace.repository.BookingRepository;
import com.datn.coworkingspace.repository.CategoryRepository;
import com.datn.coworkingspace.repository.SpacePaymentRepository;
import com.datn.coworkingspace.repository.SpaceRepository;
import com.datn.coworkingspace.utils.CommonUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class StatisticService implements IStatisticService {

    @Autowired
    private SpacePaymentRepository spacePaymentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Override
    public BigDecimal getAllRevenueByDay(String day) {
        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);
            BigDecimal total = spacePaymentRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getAllRevenueByMonth(String month) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));
            BigDecimal total = spacePaymentRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigDecimal getAllRevenueByQuarter(String quarter) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));
            BigDecimal total = spacePaymentRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigDecimal getAllRevenueByYear(String year) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);
            BigDecimal total = spacePaymentRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTotalSpaceBookingGroupByCategoryByMonthInYear1(String year) {
        List<Map<String, Object>> map = new ArrayList<>();
        try {
            long count = categoryRepository.count();

            List<Category> categories = categoryRepository.findAll();

            int i =1 ;
            while (i <= 12) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("month", i);
                Timestamp date = CommonUtils.convertStringToMonth(i + "-" + year, "-");
                Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(i + "-" + year, "-"));
                List<Map<String, Object>> listSoldByCategory = bookingRepository.getTotalSpaceBookingGroupByCategory(date, dateEndDate);
                List<Long> listCategoryId = new ArrayList<>();
                for(int j = 0 ; j < listSoldByCategory.size(); j++) {
                    listCategoryId.add(Long.parseLong(listSoldByCategory.get(j).get("categoryId").toString()));
                }
                List<Map<String, Object>> listSoldByCategoryProcess = new ArrayList<>();
                for(Category category : categories) {
                    Map<String, Object> map2 = new HashMap<>();
                    if(listCategoryId.contains(category.getId())) {
                        Optional<Map<String, Object>> ct = listSoldByCategory.stream().filter(x -> x.get("categoryId").equals(category.getId())).findFirst();
                        map2.put("categoryId", category.getId());
                        map2.put("categoryName", category.getName());
                        map2.put("quantity", ct.get().get("quantity"));
                    } else {
                        map2.put("categoryId", category.getId());
                        map2.put("categoryName", category.getName());
                        map2.put("quantity", 0);
                    }
                    listSoldByCategoryProcess.add(map2);


                }
                map1.put("soldByCategory", listSoldByCategoryProcess);
                map.add(map1);
                i++;
            }
            List<Map<String, Object>> listSoldByCategoryProcess1 = new ArrayList<>();
            for(Category category : categories) {
                Map<String, Object> map2 = new HashMap<>();

                map2.put("name", category.getName());
                List<BigDecimal> dataList = new ArrayList<>();
                for(int k = 0; k < 12; k++) {
                    List<Map<String, Object>> objectMaps = (List<Map<String, Object>>) map.get(k).get("soldByCategory");
                    Optional<Map<String, Object>> ct = objectMaps.stream().filter(x -> x.get("categoryId").equals(category.getId())).findFirst();
                    dataList.add(new BigDecimal(ct.get().get("quantity").toString()));
                }
                map2.put("data", dataList);
                listSoldByCategoryProcess1.add(map2);
            }

            return listSoldByCategoryProcess1;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalSpaceActive() {
        return spaceRepository.countSpaceActive();
    }

    @Override
    public Long getTotalBooking() {
        return bookingRepository.count();
    }

    @Override
    public List<Map<String, Object>> getAllTopSpaceByDay(String day) {
        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);
            List<Map<String, Object>> listSoldProduct = bookingRepository.getAllTopSpaceByDay(date, dateEndDate, EnumUtils.getEnum(BookingStatus.class, "CANCELLED"), PageRequest.of(0,5));

            return listSoldProduct;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> getAllTopSpaceByMonth(String month) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));
            List<Map<String, Object>> listSoldProduct = bookingRepository.getAllTopSpaceByDay(date, dateEndDate, EnumUtils.getEnum(BookingStatus.class, "CANCELLED"), PageRequest.of(0, 5));

            return listSoldProduct;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllTopSpaceByQuarter(String quarter) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));
            List<Map<String, Object>> listSoldProduct = bookingRepository.getAllTopSpaceByDay(date, dateEndDate, EnumUtils.getEnum(BookingStatus.class, "CANCELLED"), PageRequest.of(0, 5));

            return listSoldProduct;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllTopSpaceByYear(String year) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);
            List<Map<String, Object>> listSoldProduct = bookingRepository.getAllTopSpaceByDay(date, dateEndDate, EnumUtils.getEnum(BookingStatus.class, "CANCELLED"), PageRequest.of(0, 5));

            return listSoldProduct;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }
}
