package calzone;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeFormatterTest {
    private static final long FIVE_MINS = 5 * 60 * 1000;
    private static final long ONE_HOUR = FIVE_MINS * 12;
    private static final long TWO_HOURS = ONE_HOUR * 2;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long THREE_DAYS = ONE_DAY * 3;

    private TimeFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new TimeFormatter();
    }

    @Test
    public void formatsPeriodLessThanOneHour() throws Exception {
        Date date = new Date(new Date().getTime() - FIVE_MINS);
        assertThat(formatter.formatDateSince(date), is("5 Minutes"));
    }

    @Test
    public void formatsPeriodOfOneHour() throws Exception {
        Date date = new Date(new Date().getTime() - ONE_HOUR);
        assertThat(formatter.formatDateSince(date), is("1 Hour"));
    }

    @Test
    public void formatsPeriodOverOneHourLessThanOneDay() throws Exception {
        Date date = new Date(new Date().getTime() - TWO_HOURS);
        assertThat(formatter.formatDateSince(date), is("2 Hours"));
    }

    @Test
    public void formatsPeriodOfOneDay() throws Exception {
        Date date = new Date(new Date().getTime() - ONE_DAY);
        assertThat(formatter.formatDateSince(date), is("1 Day 0 Hours"));
    }

    @Test
    public void formatsPeriodOverOneDay() throws Exception {
        Date date = new Date(new Date().getTime() - THREE_DAYS);
        assertThat(formatter.formatDateSince(date), is("3 Days 0 Hours"));
    }

    @Test
    public void formatsSecondsOnly() throws Exception {
        assertThat(formatter.formatMinutesAndSeconds(55), is("0:55"));
    }

    @Test
    public void formatsMinutesAndSeconds() throws Exception {
        assertThat(formatter.formatMinutesAndSeconds(119), is("1:59"));
    }

    @Test
    public void formatsMinutesAndFewSeconds() throws Exception {
        assertThat(formatter.formatMinutesAndSeconds(65), is("1:05"));
    }

    @Test
    public void formatsManyMinutesAndSeconds() throws Exception {
        assertThat(formatter.formatMinutesAndSeconds(659), is("10:59"));
    }
}