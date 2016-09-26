package mendona.vitor.habittracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HabitList extends Activity {

    protected CalendarManager calendarManager;
    private Calendar calendar;
    private Date currentDate;
    private Map<Habit, Integer> habitsForDate;

    private List<String> habitsOnScreen;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        calendar = Calendar.getInstance();
        currentDate = new Date(calendar.getTimeInMillis());
        calendarManager = new AbstractCalendarManager(calendar, this);
        habitsForDate = calendarManager.getHabitsForDate(currentDate);
        habitsOnScreen = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, habitsOnScreen);

        ListView habitListView = (ListView) findViewById(R.id.habit_list);
        habitListView.setAdapter(adapter);

        Button addHabitButton = (Button) findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final AlertDialog.Builder builder = new AlertDialog.Builder(HabitList.this);
                final View dialogView = inflater.inflate(R.layout.add_habit_dialog, null);
                final EditText nameView = (EditText) dialogView.findViewById(R.id.add_habit_name);
                final EditText dateView = (EditText) dialogView.findViewById(R.id.add_habit_date);
                final EditText weekdaysView = (EditText) dialogView.findViewById(R.id.add_habit_weekday);
                builder.setView(dialogView);
                builder.setTitle(R.string.add_habit_dialog_title);
                builder.setPositiveButton(R.string.add_habit_OK_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final Habit newHabit = createHabit(nameView.getText().toString(), dateView.getText().toString(), weekdaysView.getText().toString());
                        calendarManager.addHabit(newHabit);
                        dialog.dismiss();
                        reloadHabitsOnScreen();
                    }
                });
                final AlertDialog addNewDialog =  builder.create();
                addNewDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadHabitsOnScreen();
    }

    private void reloadHabitsOnScreen() {
        habitsOnScreen.clear();
        habitsForDate = calendarManager.getHabitsForDate(currentDate);
        for (final Habit habit : habitsForDate.keySet()) {
            habitsOnScreen.add(habit.getName());
        }
        Collections.sort(habitsOnScreen);
        adapter.notifyDataSetChanged();
    }

    protected static Map<String, Weekday> weekdayInputTranslator;
    static {
        weekdayInputTranslator = new HashMap<>();
        weekdayInputTranslator.put("SU", Weekday.SUNDAY);
        weekdayInputTranslator.put("MO", Weekday.MONDAY);
        weekdayInputTranslator.put("TU", Weekday.TUESDAY);
        weekdayInputTranslator.put("WE", Weekday.WEDNESDAY);
        weekdayInputTranslator.put("TH", Weekday.THURSDAY);
        weekdayInputTranslator.put("FR", Weekday.FRIDAY);
        weekdayInputTranslator.put("SA", Weekday.SATURDAY);
    }

    protected Habit createHabit(final String name, final String originalDate, final String weekdays) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", getResources().getConfiguration().locale);
            final Date habitOriginalDate = sdf.parse(originalDate);

            final Set<Weekday> habitWeekdays = new HashSet<>();
            for (String word : weekdays.split(" ")) {
                final Weekday weekday = weekdayInputTranslator.get(word.toUpperCase());
                if (weekday != null) {
                    habitWeekdays.add(weekday);
                }
            }
            return new Habit(name, habitOriginalDate, habitWeekdays);
        } catch (ParseException pe) {
            throw new RuntimeException("Could not create habit: " + pe.getMessage());
        }

    }
}
