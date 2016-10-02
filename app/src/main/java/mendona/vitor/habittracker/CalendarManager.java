package mendona.vitor.habittracker;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Main controller. Updates to the model should happen through an instance
 * of this interface. The Activities should not modify the model directly.
 *
 * Only one instance per application should  exist.
 */
public interface CalendarManager {

    Map<Habit, List<Completion>> getHabitsForDate(Date calendar);

    Set<Habit> getAllHabits();

    Habit getHabitByName(String habitName);

    List<Completion> getCompletionsForHabit(String habitName);

    boolean addHabit(Habit habit);

    void deleteHabit(Habit habit);

    boolean addCompletion(Habit habit, Date date);

    void deleteCompletion(Completion completion);

    int timesHabitFulfilled(Habit habit, Date currentDate);

}
