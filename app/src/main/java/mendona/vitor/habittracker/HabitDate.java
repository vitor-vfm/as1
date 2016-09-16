package mendona.vitor.habittracker;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by vitor on 15/09/16.
 */
public class HabitDate {

    private final Calendar calendarDate;
    private final Map<Habit, Integer> habitCompletions; // values represent whether they're completed

    public HabitDate(final Calendar calendarDate) {
        this.calendarDate = calendarDate;
        habitCompletions = new HashMap<>();
    }

    public boolean hasHabit(final Habit habit) {
        return habitCompletions.containsKey(habit);
    }

    public Weekday getWeekday() {
        return new Weekday(calendarDate);
    }

    protected void addHabit(final Habit habit) {
        habitCompletions.put(habit, 0);
    }

    protected void completeHabit(final Habit habit) {
        if (!habitCompletions.containsKey(habit)) {
            throw new InvalidParameterException();
        }

        habitCompletions.put(habit, habitCompletions.get(habit) + 1);
    }

    public Set<Habit> getHabits() {
        return habitCompletions.keySet();
    }
}
