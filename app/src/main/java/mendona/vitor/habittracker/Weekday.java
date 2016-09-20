package mendona.vitor.habittracker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vitor on 19/09/16.
 */
public enum Weekday {
    SUNDAY(Calendar.SUNDAY),
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY);

    final int day;

    Weekday(final int day) {
        this.day = day;
    }

    public static Weekday fromDate(final Date date, final Calendar calendar) {
        calendar.setTime(date);
        final int toFind = calendar.get(Calendar.DAY_OF_WEEK);
        for (Weekday weekday : Weekday.values())
            if (weekday.day == toFind)
                return weekday;

        return SUNDAY;
    }
}
