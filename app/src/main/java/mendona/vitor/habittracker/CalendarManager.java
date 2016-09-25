package mendona.vitor.habittracker;

import java.util.Date;
import java.util.Map;

/**
 * Created by vitor on 15/09/16.
 */
public interface CalendarManager {

    Map<Habit, Integer> getHabitsForDate(Date calendar);

    void addHabit(Habit habit);

    void deleteHabit(Habit habit);

    void addCompletion(Habit habit, Date date);

    void deleteCompletion(Habit habit, Date date);

    int timesHabitFulfilled(Habit habit);

    int timesHabitUnfulfilled(Habit habit);

    void saveHabits();

    void saveCompletions();

}
