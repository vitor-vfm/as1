package mendona.vitor.habittracker;

import java.util.*;

/**
 * Created by vitor on 15/09/16.
 */
public class AbstractCalendarManager implements CalendarManager {

    final List<Completion> completions;
    final Set<Habit> habits;
    final Set<Calendar> datesRecorded;

    final Map<Calendar, List<Completion>> completionsInDate;
    final Map<Habit, List<Completion>> completionsForHabit;
    final Map<Weekday, Set<Habit>> habitsInWeekday;

    public AbstractCalendarManager() {
        // TODO: Add persistence

        this.completions = new ArrayList<>();
        this.habits = new HashSet<>();
        this.datesRecorded = new HashSet<>();
        this.completionsInDate = new HashMap<>();
        this.completionsForHabit = new HashMap<>();

        this.habitsInWeekday = new HashMap<>();
        for (Weekday weekday : Weekday.allWeekdays)
            habitsInWeekday.put(weekday, new HashSet<Habit>());

    }

    @Override
    public Map<Habit, Integer> getHabitsForDate(Calendar calendar) {
        final Set<Habit> habits = habitsInWeekday.get(new Weekday(calendar));
        final Map<Habit, Integer> result = new HashMap<>();
        for (Habit habit : habits) {
            result.put(habit, completionsForHabit.get(habit).size());
        }
        return result;
    }

    @Override
    public void addHabit(Habit habit) {
        habits.add(habit);
        completionsForHabit.put(habit, new ArrayList<Completion>());
    }

    @Override
    public void deleteHabit(Habit habit) {
        habits.remove(habit);
        completionsForHabit.remove(habit);
    }

    @Override
    public void addCompletion(Habit habit, Calendar date) {
        final Completion completion = new Completion(habit, date);
        completions.add(completion);

        if (!completionsForHabit.containsKey(habit))
            completionsForHabit.put(habit, new ArrayList<Completion>());
        if (!completionsInDate.containsKey(date))
            completionsInDate.put(date, new ArrayList<Completion>());

        completionsForHabit.get(habit).add(completion);
        completionsInDate.get(date).add(completion);
    }

    @Override
    public void deleteCompletion(Habit habit, Calendar date) {
        final Completion completion = new Completion(habit, date);
        completions.remove(completion);

        completionsForHabit.get(habit).remove(completion);
        completionsInDate.get(date).remove(completion);
    }

    @Override
    public int timesHabitFulfilled(Habit habit) {
        return 0;
    }

    @Override
    public int timesHabitUnfulfilled(Habit habit) {
        return 0;
    }
}
