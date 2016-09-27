package mendona.vitor.habittracker;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vitor on 15/09/16.
 */
public interface CalendarManager {

    Map<Habit, List<Completion>> getHabitsForDate(Date calendar);

    Set<Habit> getAllHabits();

    List<Completion> getCompletionsForHabit(String habitName);

    void addHabit(Habit habit);

    void deleteHabit(Habit habit);

    void addCompletion(Habit habit, Date date);

    void deleteCompletion(Habit habit, Date date);

    int timesHabitFulfilled(Habit habit);

    int timesHabitUnfulfilled(Habit habit);

}
