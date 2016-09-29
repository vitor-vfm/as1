package mendona.vitor.habittracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    private ArrayList<HabitOnScreen> habitsOnScreen;
    private HabitsOnScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        calendar = Calendar.getInstance();
        currentDate = new Date(calendar.getTimeInMillis());
        calendarManager = new AbstractCalendarManager(calendar, this, currentDate);
        habitsOnScreen = new ArrayList<>();
        adapter = new HabitsOnScreenAdapter(this, habitsOnScreen);

        final ListView habitListView = (ListView) findViewById(R.id.habit_list);
        habitListView.setAdapter(adapter);
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HabitList.this);
                dialogBuilder.setTitle(R.string.complete_habit_dialog_title);
                dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HabitOnScreen habitOnScreen = adapter.getItem(position);
                        calendarManager.addCompletion(habitOnScreen.getHabit(), currentDate);
                        reloadHabitsOnScreen();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
        habitListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        Button addHabitButton = (Button) findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final AlertDialog.Builder builder = new AlertDialog.Builder(HabitList.this);
                final View dialogView = inflater.inflate(R.layout.add_habit_dialog, null);
                final EditText nameView = (EditText) dialogView.findViewById(R.id.add_habit_name);

                final Button chooseDateButton = (Button) dialogView.findViewById(R.id.add_habit_date);
                calendar.setTime(currentDate);
                chooseDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(HabitList.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });
                final EditText weekdaysView = (EditText) dialogView.findViewById(R.id.add_habit_weekday);
                builder.setView(dialogView);
                builder.setTitle(R.string.add_habit_dialog_title);
                builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final Habit newHabit = createHabit(nameView.getText().toString(), calendar.getTime(), weekdaysView.getText().toString());
                        final boolean ok = calendarManager.addHabit(newHabit);
                        if (!ok)
                            Toast.makeText(HabitList.this, R.string.invalid_habit, Toast.LENGTH_SHORT).show();
                        else {
                            dialog.dismiss();
                            reloadHabitsOnScreen();
                        }
                    }
                });
                final AlertDialog addNewDialog =  builder.create();
                addNewDialog.show();
            }
        });

        Button seeCompletionsButton = (Button) findViewById(R.id.see_completions_button);
        seeCompletionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> habitsToShow = new ArrayList<String>();
                for (Habit habit : calendarManager.getAllHabits()) {
                    habitsToShow.add(habit.getName());
                }
                final ArrayAdapter<String> habitsToShowAdapter = new ArrayAdapter<String>(HabitList.this,
                        android.R.layout.test_list_item, habitsToShow);

                final AlertDialog.Builder chooseHabitsBuilder = new AlertDialog.Builder(HabitList.this);
                chooseHabitsBuilder.setTitle(R.string.choose_habit_to_see_completions);
                chooseHabitsBuilder.setSingleChoiceItems(habitsToShowAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String habitName = habitsToShowAdapter.getItem(which);

                        final List<Completion> completionsForHabit = calendarManager.getCompletionsForHabit(habitName);
                        final ArrayAdapter<Completion> completionsAdapter = new ArrayAdapter<Completion>(HabitList.this,
                                android.R.layout.test_list_item, completionsForHabit);

                        AlertDialog.Builder displayCompletionsDialogBuilder = new AlertDialog.Builder(HabitList.this);
                        displayCompletionsDialogBuilder.setTitle(R.string.see_completions_title);
                        displayCompletionsDialogBuilder.setAdapter(completionsAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Completion toDelete = completionsAdapter.getItem(which);
                                final AlertDialog.Builder confirmation = new AlertDialog.Builder(HabitList.this);
                                confirmation.setMessage(R.string.delete_completion_confirm);
                                confirmation.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        calendarManager.deleteCompletion(toDelete);
                                        completionsAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        reloadHabitsOnScreen();
                                    }
                                });
                                confirmation.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                confirmation.show();
                            }
                        });
                        displayCompletionsDialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        displayCompletionsDialogBuilder.create().show();
                    }
                });
                chooseHabitsBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                chooseHabitsBuilder.show();
            }
        });

        Button deleteHabitsButton = (Button) findViewById(R.id.delete_habit_button);
        deleteHabitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> habitsToDelete = new ArrayList<String>();
                for (Habit habit : calendarManager.getAllHabits()) {
                    habitsToDelete.add(habit.getName());
                }
                final ArrayAdapter<String> habitsToDeleteAdapter = new ArrayAdapter<String>(HabitList.this,
                        android.R.layout.test_list_item, habitsToDelete);

                final AlertDialog.Builder builder = new AlertDialog.Builder(HabitList.this);
                builder.setTitle(R.string.choose_habit);
                builder.setSingleChoiceItems(habitsToDeleteAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(HabitList.this);
                        final Habit toDelete = calendarManager.getHabitByName(habitsToDeleteAdapter.getItem(which));
                        confirmationBuilder.setMessage(R.string.delete_habit_confirm);
                        confirmationBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                calendarManager.deleteHabit(toDelete);
                                habitsToDelete.remove(toDelete.getName());
                                habitsToDeleteAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                reloadHabitsOnScreen();
                            }
                        });
                        confirmationBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        confirmationBuilder.show();
                    }
                });
                builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        Button showStatsButton = (Button) findViewById(R.id.show_stats_button);
        showStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Set<Habit> habitsToShow = calendarManager.getAllHabits();
                final List<String> habitNames = new ArrayList<String>();
                for (Habit habit : habitsToShow) {
                    habitNames.add(habit.getName());
                }

                final ArrayAdapter<String> habitNamesAdapter = new ArrayAdapter<String>(HabitList.this,
                        android.R.layout.simple_list_item_1, habitNames);

                final AlertDialog.Builder habitsDialog = new AlertDialog.Builder(HabitList.this);
                habitsDialog.setAdapter(habitNamesAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Habit habitToShowStats = calendarManager.getHabitByName(habitNamesAdapter.getItem(which));
                        final int timesFulfilled = calendarManager.timesHabitFulfilled(habitToShowStats);
                        final int timesCompleted = calendarManager.getCompletionsForHabit(habitToShowStats.getName()).size();
                        final String displayMessage = getString(R.string.times_habit_completed) + timesCompleted + "\n" +
                                getString(R.string.times_habit_fulfilled) + timesFulfilled;
                        final AlertDialog.Builder statsDialog = new AlertDialog.Builder(HabitList.this);
                        statsDialog.setMessage(displayMessage);
                        statsDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        statsDialog.show();
                    }
                });
                habitsDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                habitsDialog.show();
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
        final Map<Habit, List<Completion>> habitsForDate = calendarManager.getHabitsForDate(currentDate);
        for (final Map.Entry<Habit, List<Completion>> e : habitsForDate.entrySet()) {
            habitsOnScreen.add(new HabitOnScreen(e.getKey(), e.getValue().size()));
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

    protected Habit createHabit(final String name, final Date originalDate, final String weekdays) {
        final Set<Weekday> habitWeekdays = new HashSet<>();
        for (String word : weekdays.split(" ")) {
            final Weekday weekday = weekdayInputTranslator.get(word.toUpperCase());
            if (null == weekday) {
                return null;
            } else {
                habitWeekdays.add(weekday);
            }
        }
        return new Habit(name, calendarManager.getFormattedDate(originalDate), habitWeekdays);
    }
}
