package mendona.vitor.habittracker;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vitor on 15/09/16.
 */
public interface CalendarManager {

    String getFormattedDate(Date date);

    Map<Habit, List<Completion>> getHabitsForDate(Date calendar);

    Set<Habit> getAllHabits();

    Habit getHabitByName(String habitName);

    List<Completion> getCompletionsForHabit(String habitName);

    boolean addHabit(Habit habit);

    void deleteHabit(Habit habit);

    boolean addCompletion(Habit habit, Date date);

    void deleteCompletion(Completion completion);

    int timesHabitFulfilled(Habit habit);

}
