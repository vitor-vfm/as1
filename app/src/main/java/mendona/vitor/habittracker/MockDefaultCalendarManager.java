package mendona.vitor.habittracker;

import java.util.Calendar;

/**
 * Created by vitor on 29/09/16.
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
