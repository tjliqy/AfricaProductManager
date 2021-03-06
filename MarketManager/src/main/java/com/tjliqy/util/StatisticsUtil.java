package com.tjliqy.util;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tjliqy on 2017/5/10.
 */
public class StatisticsUtil {
    public static List<BigDecimal> getMoneyValueList(List<Record> months, List<String> monthsList) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        List<BigDecimal> valueList = new ArrayList<>();
        int i = 0;
        if (months.size() > 0) {
            for (Record r : months) {
                String time = (String) r.getColumns().get("month_num");
                for (; i < monthsList.size();i++ ) {
                    if (monthsList.get(i).equals(time)) {
                        valueList.add((BigDecimal) r.getColumns().get("month_cost"));
                        break;
                    }else {
                        valueList.add(BigDecimal.ZERO);
                    }
                }
            }
        }
        for (;i<monthsList.size();i++){
            valueList.add(BigDecimal.ZERO);
        }
        return valueList;
    }
    public static List<BigDecimal> getNumValueList(List<Record> months, List<String> monthsList) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        List<BigDecimal> valueList = new ArrayList<>();
        int i = 0;

        if (months.size() > 0) {
            for (Record r : months) {
                String time = (String) r.getColumns().get("month_num");
                for (; i < monthsList.size();i++ ) {
                    if (monthsList.get(i).equals(time)) {
                        valueList.add((BigDecimal) r.getColumns().get("month_quantity"));
                        break;
                    }else {
                        valueList.add(BigDecimal.ZERO);
                    }

                }
            }
        }
        for (;i<monthsList.size();i++){
            valueList.add(BigDecimal.ZERO);
        }
        return valueList;
    }


    public static List<String> getMonthList(List<Record> months, String minTime, String larTime) {
        List<String> monthsList = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM");

        Date minDate = null, larDate = null;

        try {
            if (!StrKit.isBlank(minTime)) {
                minDate = df.parse(minTime);
            }
            if (!StrKit.isBlank(larTime)) {
                larDate = df.parse(larTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            if (months.size() > 0) {
                if (minDate == null) {

                    minDate = df.parse(months.get(0).get("month_num").toString().substring(0,5) + "01");

                }
                if (larDate == null) {
                    larDate = df.parse(months.get(months.size() -1).get("month_num").toString().substring(0,5)+ "12");

                }

                Calendar c = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.setTime(larDate);
                for (c.setTime(minDate); !c.after(end); c.add(Calendar.MONTH, 1)) {
                    String format = df.format(c.getTime());
                    monthsList.add(format);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthsList;
    }
}
