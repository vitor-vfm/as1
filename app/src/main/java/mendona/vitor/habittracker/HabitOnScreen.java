package mendona.vitor.habittracker;

/**
 * Created by vitor on 26/09/16.
 */
public class HabitOnScreen implements Comparable<HabitOnScreen> {
    // Represents a habit as it appears on screen
    private Habit habit;
    private int completionCount;

    public HabitOnScreen(final Habit habit, int count) {
        this.habit = habit;
        this.completionCount = count;
    }

    public Habit getHabit() {
        return habit;
    }

    public int getCompletionCount() {
        return completionCount;
    }

    @Override
    public int compareTo(HabitOnScreen another) {
        if (another == null)
            return -1;

        return getHabit().getName().compareTo(another.getHabit().getName());
    }

    public void incrementCount() {
        completionCount++;
    }

    public void decrementCount() {
        if (completionCount == 0)
            throw new RuntimeException("Attempted to decrement completions when count was zero");

        completionCount--;
    }
}
