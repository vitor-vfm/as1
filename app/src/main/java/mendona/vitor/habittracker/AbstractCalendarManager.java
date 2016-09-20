package mendona.vitor.habittracker;

import java.util.*;

/**
 * Created by vitor on 15/09/16.
 */
public class AbstractCalendarManager implements CalendarManager {

    final List<Completion> completions;
    final Set<Habit> habits;
    final Set<Date> datesRecorded;

    final Map<Weekday, Set<Habit>> habitsInWeekday;
    final Calendar calendar;

    final Map<Date, Map<Habit, List<Completion>>> cache;

    public AbstractCalendarManager(final Calendar calendar) {
        // TODO: Add persistence

        this.completions = new ArrayList<>();
        this.habits = new HashSet<>();
        this.datesRecorded = new HashSet<>();
        this.cache = new HashMap<>();
        this.calendar = calendar;

        this.habitsInWeekday = new HashMap<>();
        for (Weekday weekday : Weekday.values())
            habitsInWeekday.put(weekday, new HashSet<Habit>());
        for (Habit habit : habits)
            habitsInWeekday.get(habit.getWeekday()).add(habit);

    }

    private void buildResultForDate(Date date) {
        final Set<Habit> habitsForDate = habitsInWeekday.get(Weekday.fromDate(date, calendar));
        final Map<Habit, List<Completion>> entry = new HashMap<>();

        for (Completion completion : completions) {
            if (entry.containsKey(completion.getHabit()))
                entry.get(completion.getHabit()).add(completion);
            else
                entry.put(completion.getHabit(), new ArrayList<Completion>());
        }

        entry.keySet().retainAll(habitsForDate);

        cache.put(date, entry);
    }

    @Override
    public Map<Habit, Integer> getHabitsForDate(Date date) {
        if (!cache.containsKey(date)) {
            datesRecorded.add(date);
            buildResultForDate(date);
        }

        final Map<Habit, Integer> result = new HashMap<>();
        for (Map.Entry<Habit, List<Completion>> entry : cache.get(date).entrySet())
            result.put(entry.getKey(), entry.getValue().size());
        return result;
    }

    private void clearCache(Habit habit) {
        final Set<Date> datesInvalid = new HashSet<>();

        for (Date date : cache.keySet())
            if (cache.get(date).containsKey(habit))
                datesInvalid.add(date);

        cache.keySet().removeAll(datesInvalid);
    }

    private void clearCache(Date date) {
        cache.keySet().remove(date);
    }

    @Override
    public void addHabit(Habit habit) {
        habits.add(habit);
        habitsInWeekday.get(habit.getWeekday()).add(habit);

        clearCache(habit);
    }

    @Override
    public void deleteHabit(Habit habit) {
        habits.remove(habit);
        habitsInWeekday.get(habit.getWeekday()).remove(habit);

        clearCache(habit);
    }

    @Override
    public void addCompletion(Habit habit, Date date) {
        final Completion completion = new Completion(habit, date);
        completions.add(completion);

        clearCache(date);
    }

    @Override
    public void deleteCompletion(Habit habit, Date date) {
        final Completion completion = new Completion(habit, date);
        completions.remove(completion);

        clearCache(date);
    }

    @Override
    public int timesHabitFulfilled(Habit habit) {
        int timesFulfilled = 0;
        for (Date date : datesRecorded) {
            final Integer completionsInDate = getHabitsForDate(date).get(habit);
            if (completionsInDate != null && completionsInDate > 0)
                timesFulfilled++;
        }
        return timesFulfilled;
    }

    @Override
    public int timesHabitUnfulfilled(Habit habit) {
        int timesUnfulfilled = 0;
        for (Date date : datesRecorded) {
            final Integer completionsInDate = getHabitsForDate(date).get(habit);
            if (completionsInDate != null && completionsInDate == 0)
                timesUnfulfilled++;
        }
        return timesUnfulfilled;
    }
}
