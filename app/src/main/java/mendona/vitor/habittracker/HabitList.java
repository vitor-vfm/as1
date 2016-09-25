package mendona.vitor.habittracker;

import android.app.Activity;
import android.os.SystemClock;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HabitList extends Activity {

    private CalendarManager calendarManager;
    private Calendar calendar;
    private Date currentDate;
    private Map<Habit, Integer> habitsForDate;

    private List<String> habitsOnScreen;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        currentDate = new Date(SystemClock.currentThreadTimeMillis());
        calendar = Calendar.getInstance();
        calendarManager = new AbstractCalendarManager(calendar);
        habitsForDate = calendarManager.getHabitsForDate(currentDate);
        habitsOnScreen = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, habitsOnScreen);

        ListView habitListView = (ListView) findViewById(R.id.habit_list);
        habitListView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadHabitsOnScreen();
    }

    private void reloadHabitsOnScreen() {
        habitsOnScreen.clear();
        for (final Habit habit : habitsForDate.keySet()) {
            habitsOnScreen.add(habit.getName());
        }
        Collections.sort(habitsOnScreen);
    }
}
