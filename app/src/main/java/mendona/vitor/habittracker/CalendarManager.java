package mendona.vitor.habittracker;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by vitor on 15/09/16.
 */
public interface CalendarManager {

    Map<Habit, Integer> getHabitsForDate(Calendar calendar);

    void addHabit(Habit habit);

    void deleteHabit(Habit habit);

    void addCompletion(Habit habit, Calendar date);

    void deleteCompletion(Habit habit, Calendar date);

    int timesHabitFulfilled(Habit habit);

    int timesHabitUnfulfilled(Habit habit);

}
