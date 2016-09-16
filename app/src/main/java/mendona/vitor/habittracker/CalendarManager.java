package mendona.vitor.habittracker;

import java.util.*;

/**
 * Created by vitor on 15/09/16.
 */
public class CalendarManager {

    private Map<Weekday, Set<Habit>> habitsForWeekday;
    private Map<Calendar, HabitDate> datesInApp;

    public CalendarManager() {
        this.datesInApp = new HashMap<>();

        this.habitsForWeekday = new HashMap<>();
        for (final Weekday day : Weekday.allWeekdays) {
            habitsForWeekday.put(day, new HashSet<Habit>());
        }
    }


    private HabitDate createDate(final Calendar calendar) {
        final HabitDate habitDate = new HabitDate(calendar);
        final Set<Habit> habits = habitsForWeekday.get(habitDate.getWeekday());
        if (habits == null)
            throw new RuntimeException("Calendar Manager habit map is missing key " + habitDate.getWeekday());

        for (final Habit habit : habits)
            habitDate.addHabit(habit);

        return habitDate;
    }

    public void addHabits(final Collection<Habit> habits) {
        for (final Habit habit : habits) {
            habitsForWeekday.get(habit.weekDay).add(habit);
        }
    }

    public Set<Habit> getHabitsInDate(final Calendar calendar) {
        if (!datesInApp.containsKey(calendar))
            datesInApp.put(calendar, createDate(calendar));

        return datesInApp.get(calendar).getHabits();
    }

}
