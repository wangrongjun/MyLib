package com.wang.java_util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class DateUtil {

    /**
     * 获取系统当前日期，格式：yyyy-MM-dd
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取系统当前时间，格式：HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取系统当前日期和时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateAndTime() {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * @param date1 输入如2016-05-20或2016-5-20
     * @return date1与date2相隔的天数，负数则date1 < date2。依次类推。
     */
    public static int compareDate(String date1, String date2) throws Exception {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        String[] date11 = date1.split("-");
        String[] date22 = date2.split("-");
        calendar1.set(Integer.parseInt(date11[0]), Integer.parseInt(date11[1]),
                Integer.parseInt(date11[2]), 0, 0, 0);
        calendar2.set(Integer.parseInt(date22[0]), Integer.parseInt(date22[1]),
                Integer.parseInt(date22[2]), 0, 0, 0);

        long timeInMillis = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();
        return (int) (timeInMillis / 1000 / 60 / 60 / 24);
//        return calendar1.compareTo(calendar2);

    }

    /**
     * 推迟或提早日期。如2016-07-08，天数为4，则输出2016-07-12
     *
     * @param day 可以为负数
     */
    public static String changeDate(String date, int day) throws Exception {
        Calendar calendar = Calendar.getInstance();
        String[] split = date.split("-");
        calendar.set(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1,
                Integer.parseInt(split[2]), 0, 0, 0);

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd");
        //注意！！！1000L中最后的L一定要加，否则long+int的结果会出错！！！
        return sdf.format(new Date(calendar.getTimeInMillis() + day * 24 * 60 * 60 * 1000L));
    }

    /**
     * 获得网络日期 格式：2016-01-31
     */
    public static String getInternetDate() {
        String baiduWeatherUrl = "http://api.map.baidu.com/telematics/v3/weather?" +
                "output=json&ak=6tYzTvGZSOpYB5Oc2YGGOKt8&location=";
        try {
            URL url = new URL(baiduWeatherUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);

            InputStreamReader isr = new InputStreamReader(
                    conn.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
            br.close();
            isr.close();

            String html = builder.toString();
            int i = html.indexOf("date\":\"");
            String date = html.substring(i + 7).substring(0, 10);
            return date;
        } catch (Exception e) {
            return "";
        }
    }

    public static long getTimeInMillis(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis();
    }

    public static String toDateTimeText(Date date) {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * @param dateText 格式如：yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
     */
    public static Date toDate(String dateText) throws DateTextSyntaxErrorException {
        if (!dateText.matches("[\\d]+-[\\d]+-[\\d]+") &&
                !dateText.matches("[\\d]+-[\\d]+-[\\d]+ [\\d]+:[\\d]+:[\\d]+")) {
            throw new DateTextSyntaxErrorException(dateText);
        }

        Calendar calendar = Calendar.getInstance();
        try {
            if (dateText.contains(" ")) {
                String[] strings = dateText.split(" ");
                String[] dateList = strings[0].split("-");
                int year = Integer.parseInt(dateList[0]);
                int month = Integer.parseInt(dateList[1]);
                int day = Integer.parseInt(dateList[2]);
                String[] timeList = strings[1].split(":");
                int hour = Integer.parseInt(timeList[0]);
                int minute = Integer.parseInt(timeList[1]);
                int second = Integer.parseInt(timeList[2]);
                calendar.set(year, month - 1, day, hour, minute, second);
            } else {
                String[] dateList = dateText.split("-");
                int year = Integer.parseInt(dateList[0]);
                int month = Integer.parseInt(dateList[1]);
                int day = Integer.parseInt(dateList[2]);
                calendar.set(year, month - 1, day, 0, 0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DateTextSyntaxErrorException(dateText);
        }
        return calendar.getTime();
    }

    public static class DateTextSyntaxErrorException extends RuntimeException {
        DateTextSyntaxErrorException(String date) {
            super(date);
        }
    }

    @Test
    public void testToDateAndToDateTimeText() throws DateTextSyntaxErrorException {
        assertEquals("2015-12-03 12:34:10", toDateTimeText(toDate("2015-12-03 12:34:10")));//正常
        assertEquals("2017-02-03 00:54:30", toDateTimeText(toDate("2017-02-03 00:54:30")));//正常
        try {
            toDateTimeText(toDate("2015-01-03_12:34:10"));//格式错误，应该抛异常
            fail("should throw DateTextSyntaxErrorException, actually doesn't throw");
        } catch (DateTextSyntaxErrorException e) {
            e.printStackTrace();
        }
        try {
            toDateTimeText(toDate("2015-01-a3 12:34:10"));//格式错误，应该抛异常
            fail("should throw DateTextSyntaxErrorException, actually doesn't throw");
        } catch (DateTextSyntaxErrorException e) {
            e.printStackTrace();
        }
    }

}
