package calzone;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.Days.days;
import static org.joda.time.Hours.hours;
import static org.junit.Assert.assertThat;

public class DayAndHourSinceFormatterTest {

    private DayAndHourSinceFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new DayAndHourSinceFormatter();
    }

    @Test
    public void formatsPeriodLessThanOneHour() throws Exception {
        assertThat(formatter.formatDateSince(new Date()), is("0 Hours"));
    }

    @Test
    public void formatsPeriodOfOneHour() throws Exception {
        Date date = new DateTime().minus(hours(1)).toDate();
        assertThat(formatter.formatDateSince(date), is("1 Hour"));
    }

    @Test
    public void formatsPeriodOverOneHourLessThanOneDay() throws Exception {
        Date date = new DateTime().minus(hours(2)).toDate();
        assertThat(formatter.formatDateSince(date), is("2 Hours"));
    }

    @Test
    public void formatsPeriodOfOneDay() throws Exception {
        Date date = new DateTime().minus(days(1)).toDate();
        assertThat(formatter.formatDateSince(date), is("1 Day 0 Hours"));
    }

    @Test
    public void formatsPeriodOverOneDay() throws Exception {
        Date date = new DateTime().minus(days(3)).toDate();
        assertThat(formatter.formatDateSince(date), is("3 Days 0 Hours"));

    }
}
