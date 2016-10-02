package mendona.vitor.habittracker;

import java.security.InvalidParameterException;
import java.util.Date;
/**
 * Stores information about a completion event. Includes the corresponding habit
 * and the date where it was completed.
 *
 * It's meant to be immutable.
 */

public class Completion {
    private Habit habit;
    private HabitDate date;

    public Completion(final Habit habit, final Date date) {
        if (habit == null)
            throw new InvalidParameterException("Habit is null");
        if (date == null)
            throw new InvalidParameterException("Date is null");

        this.habit = habit;
        this.date = new HabitDate(date);
    }

    public Habit getHabit() {
        return habit;
    }

    public HabitDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Completion that = (Completion) o;

        if (!habit.equals(that.habit)) return false;
        return date.equals(that.date);

    }

    @Override
    public int hashCode() {
        int result = habit.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Date: " + date + "; Habit: " + habit;
    }
}
