package org.openmrs.module.msfcore.patientSummary;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

public class Age extends org.openmrs.module.reporting.common.Age {

    @Setter
    @Getter
    @JsonIgnore
    private SimpleDateFormat dateFormat;

    public Age(Date birthDate, Date currentDate, SimpleDateFormat dateFormat) {
        super(birthDate, currentDate);
        setDateFormat(dateFormat);
    }

    /**
     * @return age
     */
    public String getAge() {
        String ageString = "";
        int age = 0;
        if (getFullDays() <= getCurrentDateTime().getDayOfMonth()) {
            age = getFullDays();
            ageString = getFullDays() + " day";
        } else if (getFullMonths() <= 12) {
            age = getFullMonths();
            ageString = getFullMonths() + " month";
        } else {
            age = getFullYears();
            ageString = getFullYears() + " year";
        }
        return age > 1 ? ageString + "s" : ageString;
    }

    public String getFormattedBirthDate() {
        return getDateFormat().format(getBirthDate());
    }

    private DateTime getBirthDateTime() {
        return new DateTime(getBirthDate());
    }

    private DateTime getCurrentDateTime() {
        return new DateTime(getCurrentDate());
    }

    /**
     * Return the age in full days
     */
    public Integer getFullDays() {
        if (getBirthDate() != null) {
            return Days.daysBetween(getBirthDateTime().withTimeAtStartOfDay(), getCurrentDateTime().withTimeAtStartOfDay()).getDays();
        }
        return null;
    }
}
