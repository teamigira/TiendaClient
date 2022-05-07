/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nkanabo.Tienda;

import java.util.Date;

/**
 *
 * @author Nkanabo
 */
public class DateRangeValidator {
     private final Date startDate;
    private final Date endDate;

    public DateRangeValidator(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // inclusive startDate and endDate
    // the equals ensure the inclusive of startDate and endDate,
    // if prefer exclusive, just delete the equals
    public boolean isWithinRange(Date testDate) {

        // it works, alternatives
        /*boolean result = false;
        if ((testDate.equals(startDate) || testDate.equals(endDate)) ||
                (testDate.after(startDate) && testDate.before(endDate))) {
            result = true;
        }
        return result;*/

        // compare date and time, inclusive of startDate and endDate
        // getTime() returns number of milliseconds since January 1, 1970, 00:00:00 GMT
        return testDate.getTime() >= startDate.getTime() &&
                testDate.getTime() <= endDate.getTime();
    }
}
