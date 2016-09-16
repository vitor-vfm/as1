package mendona.vitor.habittracker;

import java.util.Calendar;

/**
 * Created by vitor on 15/09/16.
 */
public class Completion {
    public Habit habit;
    public Calendar date;

    public Completion(final Habit habit, final Calendar date) {
        this.habit = habit;
        this.date = date;
    }
}
