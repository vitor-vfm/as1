package mendona.vitor.habittracker;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by vitor on 15/09/16.
 */
public class Weekday {

    private static List<Weekday> allWeekdays() {
        final List<Weekday> result = new ArrayList<>();
        for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
            result.add(new Weekday(day));
        }
        return result;
    }

    public static final List<Weekday> allWeekdays = allWeekdays();

    public static boolean isValid(int possibleValue) {
        return Calendar.SUNDAY <= possibleValue && possibleValue <= Calendar.SATURDAY;
    }

    final int value;

    public Weekday(int value) {
        if (!isValid(value))
            throw new InvalidParameterException("Integer " + value + " out of bounds");

        this.value = value;
    }

    public Weekday(final Calendar calendar) {
        this(calendar.get(Calendar.DAY_OF_WEEK));
    }
}
