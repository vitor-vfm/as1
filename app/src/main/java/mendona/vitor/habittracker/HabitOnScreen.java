package mendona.vitor.habittracker;

/**
 * Used by the main activity to display the habits for the day.
 * Represents a habit and how many times it has been completed in the day.
 * Includes its own layout
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
