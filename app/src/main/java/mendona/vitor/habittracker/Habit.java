package mendona.vitor.habittracker;

import java.security.InvalidParameterException;
import java.util.Date;

public class Habit {

    private final String name;
    private final Date originalDate;
    private final Weekday weekday;

    public Habit(final String name, final Date originalDate, final Weekday weekday) {
        if (name == null)
            throw new InvalidParameterException("name is null");
        if (originalDate == null)
            throw new InvalidParameterException("originalDate is null");
        if (weekday == null)
            throw new InvalidParameterException("weekday is null");

        this.name = name;
        this.originalDate = originalDate;
        this.weekday = weekday;
    }

    public String getName() {
        return name;
    }

    public Date getOriginalDate() {
        return (Date) originalDate.clone();
    }

    public Weekday getWeekday() {
        return weekday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Habit habit = (Habit) o;

        if (!weekday.equals(habit.weekday)) return false;
        if (!name.equals(habit.name)) return false;
        return originalDate.equals(habit.originalDate);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + originalDate.hashCode();
        result = 31 * result + weekday.hashCode();
        return result;
    }
}
