package mendona.vitor.habittracker;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Habit {

    // Represents a habit. This class should be immutable

    private final String name;
    private final String originalDate; // dd-MM-yyyy
    private final Set<Weekday> weekdays;

    public Habit(final String name, final String originalDate, final Set<Weekday> weekdays) {
        if (name == null)
            throw new InvalidParameterException("name is null");
        if (originalDate == null)
            throw new InvalidParameterException("originalDate is null");
        if (weekdays == null)
            throw new InvalidParameterException("weekdays is null");

        this.name = name;
        this.originalDate = originalDate;
        this.weekdays = weekdays;
    }

    public String getName() {
        return name;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public Set<Weekday> getWeekdays() {
        return new HashSet<>(weekdays);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Habit habit = (Habit) o;

        if (!weekdays.equals(habit.weekdays)) return false;
        if (!name.equals(habit.name)) return false;
        return originalDate.equals(habit.originalDate);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + originalDate.hashCode();
        result = 31 * result + weekdays.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(name);
        for (Weekday weekday : weekdays) {
            builder.append(weekday);
        }
        return builder.toString();
    }
}
