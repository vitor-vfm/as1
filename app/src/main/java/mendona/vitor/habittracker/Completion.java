package mendona.vitor.habittracker;

import java.security.InvalidParameterException;
import java.util.Date;

/**
 * Created by vitor on 15/09/16.
 */
public class Completion {
    private Habit habit;
    private Date date;

    public Completion(final Habit habit, final Date date) {
        if (habit == null)
            throw new InvalidParameterException("Habit is null");
        if (date == null)
            throw new InvalidParameterException("Date is null");

        this.habit = habit;
        this.date = date;
    }

    public Habit getHabit() {
        return habit;
    }

    public Date getDate() {
        return (Date) date.clone();
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
}
