package mendona.vitor.habittracker;

import java.util.Calendar;

/**
 * A subclass of DefaultCalendarManager that removes all I/O side effects,
 * to be used for unit tests
 */
public class MockDefaultCalendarManager extends DefaultCalendarManager {

    public MockDefaultCalendarManager() {
        super(null, Calendar.getInstance().getTime(), "dummy", "dummy");
    }

    @Override
    protected void loadDataFromHabitsFile() { }

    @Override
    protected void loadDataFromCompletionsFile() { }

    @Override
    protected void saveHabits() { }

    @Override
    protected void saveCompletions() { }

}
