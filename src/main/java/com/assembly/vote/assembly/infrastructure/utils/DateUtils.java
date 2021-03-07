package com.assembly.vote.assembly.infrastructure.utils;

import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtils {
    public static Date localTimeToDate(LocalTime localTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(0, 0, 0, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        return calendar.getTime();
    }

    public static LocalDateTime toLocalDtFromString(String date){
        var dt = toDate(date);
        return convertToLocalDateTimeViaInstant(dt);
    }

    @SneakyThrows
    public static Date toDate(String date){
        SimpleDateFormat formatter;
        if(date.length()>5) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else{
            formatter = new SimpleDateFormat("HH:mm");
        }
        return formatter.parse(date);
    }

    @SneakyThrows
    public static String toString(Date date){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  dateFormat.format(date);
    }

    public static String toStringLDT(LocalDateTime dateToConvert){
        var dtf  = dateToConvert.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return  dtf;
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
