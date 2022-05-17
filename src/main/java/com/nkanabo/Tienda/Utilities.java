/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nkanabo.Tienda;

import static com.nkanabo.Tienda.Main.KeyExpiryDays;
import java.awt.Color;
import java.awt.Component;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Nkanabo
 */

public final class Utilities {

    public Utilities() throws ParseException {
        this.setExpire();
        this.setDate();
    }

    public static String expiredate;

    public static String date;

    static public String addOneDay() {
        return LocalDate.parse(date).plusDays(KeyExpiryDays).toString();
    }

    /**
     *
     * @param z
     * @return
     */
    public static int IntegerConverter(String z) {
        Integer s = 0;
        try {
            s = Integer.parseInt(z);
        } catch (NumberFormatException pe) {
            System.out.println("No number provided");
        }
        return s;
    }

    public static void Notifications(String type, String message) {
//        JOptionPane.showMessageDialog(new HomeFrame, "Hello Java");
    }

    public void setExpire() throws ParseException {
        long b;
        b = milliConverter(addOneDay());
        Utilities.expiredate = String.valueOf(b);
    }

    public String getExpire() {
        return Utilities.expiredate;
    }

    public void setDate() throws ParseException {
        LocalDate d = java.time.LocalDate.now();
        String b = d.toString();
        long c = milliConverter(b);
        Utilities.date = String.valueOf(c);
    }

    private static volatile SecureRandom numberGenerator = null;
    private static final long MSB = 0x8000000000000000L;

    public static String unique() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }

        return Long.toHexString(MSB | ng.nextLong())
                + Long.toHexString(MSB | ng.nextLong());
    }

    public static void DateCompariosn(String date1, String date2) {
        try {

            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdformat.parse(date1);
            Date d2 = sdformat.parse(date2);

            LocalDate d3 = LocalDate.now(ZoneId.of("Europe/Paris"));
            System.out.println("The date 1 is: " + sdformat.format(d1));
            System.out.println("The date 2 is: " + sdformat.format(d2));
            if (d1.compareTo(d2) > 0) {
                System.out.println(d2 + "Date 1 occurs after Date 2" + d1);
            } else if (d1.compareTo(d2) < 0) {
                System.out.println(d2 + "Date 1 occurs before Date 2" + d1);
            } else if (d1.compareTo(d2) == 0) {
                System.out.println(d2 + "Both dates are equal" + d1);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static long milliConverter(String mydate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(mydate);
        long millis = date.getTime();
        return millis;
    }

    public static String DateMilli(String milliSeconds) {

        long milliSecond = Long.parseLong(milliSeconds);
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(milliSecond);
        return "" + dateFormat.format(date);
    }

    public static class HighlightRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            // everything as usual
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // added behavior
            if (row == table.getSelectedRow()) {

                // this will customize that kind of border that will be use to highlight a row
                setBorder(BorderFactory.createMatteBorder(2, 1, 2, 1, Color.BLACK));
            }

            return this;
        }
    }

    /**
     * // 2015/09/27 15:07:53 System.out.println( new
     * SimpleDateFormat("yyyy/MM/dd
     * HH:mm:ss").format(Calendar.getInstance().getTime()) );
     *
     * // 15:07:53 System.out.println( new
     * SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
     *
     * // 09/28/2015 System.out.println(new
     * SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()));
     *
     * // 20150928_161823 System.out.println( new
     * SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())
     * );
     *
     * // Mon Sep 28 16:24:28 CEST 2015 System.out.println(
     * Calendar.getInstance().getTime() );
     *
     * // Mon Sep 28 16:24:51 CEST 2015 System.out.println( new
     * Date(System.currentTimeMillis()) );
     *
     * // Mon Sep 28 System.out.println( new Date().toString().substring(0, 10)
     * );
     *
     * // 2015-09-28 System.out.println( new
     * java.sql.Date(System.currentTimeMillis()) );
     *
     * // 14:32:26 Date d = new Date(); System.out.println( (d.getTime() / 1000
     * / 60 / 60) % 24 + ":" + (d.getTime() / 1000 / 60) % 60 + ":" +
     * (d.getTime() / 1000) % 60 );
     *
     * // 2015-09-28 17:12:35.584 System.out.println( new
     * Timestamp(System.currentTimeMillis()) );
     *
     * // Java 8
     *
     * // 2015-09-28T16:16:23.308+02:00[Europe/Belgrade] System.out.println(
     * ZonedDateTime.now() );
     *
     * // Mon, 28 Sep 2015 16:16:23 +0200 System.out.println(
     * ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME) );
     *
     * // 2015-09-28 System.out.println(
     * LocalDate.now(ZoneId.of("Europe/Paris")) ); // rest zones id in ZoneId
     * class
     *
     * // 16 System.out.println( LocalTime.now().getHour() );
     *
     * // 2015-09-28T16:16:23.315 System.out.println( LocalDateTime.now() );
      *
     */
}
