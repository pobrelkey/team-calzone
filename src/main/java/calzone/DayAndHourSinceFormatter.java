package calzone;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.Date;

public class DayAndHourSinceFormatter {
    public String formatDateSince(Date lastGoodBuildDate) {
        StringBuilder timeSinceLastGoodBuild = new StringBuilder();
        Period period = new Interval(new DateTime(lastGoodBuildDate), new DateTime()).toPeriod();
        int days = period.getDays();
        if (days > 0) {
            timeSinceLastGoodBuild.append(String.format("%d Day%s ", days, days > 1 ? "s" : ""));
        }
        int hours = period.getHours();
        timeSinceLastGoodBuild.append(String.format("%d Hour%s", hours, hours != 1 ? "s" : ""));
        return timeSinceLastGoodBuild.toString();
    }
}
