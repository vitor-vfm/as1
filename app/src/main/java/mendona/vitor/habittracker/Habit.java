package mendona.vitor.habittracker;

import java.util.Calendar;

public class Habit {

    public final String name;
    public final Calendar originalDate;
    public final Weekday weekDay;

    public Habit(final String name, final Calendar originalDate, final Weekday weekDay) {
        this.name = name;
        this.originalDate = originalDate;
        this.weekDay = weekDay;
    }

}
