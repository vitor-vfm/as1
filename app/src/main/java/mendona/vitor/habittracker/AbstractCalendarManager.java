package mendona.vitor.habittracker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vitor on 15/09/16.
 */
public class AbstractCalendarManager implements CalendarManager {

    private static final String HABIT_FILENAME = "habits.dat";
    private static final String COMPLETION_FILENAME = "completion.dat";

    final List<Completion> completions;
    final Set<Habit> habits;
    final Set<String> datesRecorded;

    final Map<Weekday, Set<Habit>> habitsInWeekday;
    final Calendar calendar;
    final Context context;
    final SimpleDateFormat simpleDateFormat;

    final Map<String, Map<Habit, List<Completion>>> cache;

    public AbstractCalendarManager(final Calendar calendar, final Context context) {

        this.completions = new ArrayList<Completion>();
        this.habits = new HashSet<Habit>();
        this.datesRecorded = new HashSet<String>();
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", context.getResources().getConfiguration().locale);
        this.cache = new HashMap<String, Map<Habit, List<Completion>>>();
        this.calendar = calendar;
        this.context = context;

        loadDataFromFile();

        this.habitsInWeekday = new HashMap<Weekday, Set<Habit>>();
        for (Weekday weekday : Weekday.values())
            habitsInWeekday.put(weekday, new HashSet<Habit>());
        for (Habit habit : habits)
            for (Weekday weekday : habit.getWeekdays())
                habitsInWeekday.get(weekday).add(habit);

    }

    private void buildResultForDate(final String date, final Weekday weekday) {
        final Map<Habit, List<Completion>> entry = new HashMap<Habit, List<Completion>>();

        for (Habit habit : habitsInWeekday.get(weekday)) {
            entry.put(habit, new ArrayList<Completion>());
        }

        for (Completion completion : completions) {
            if (entry.containsKey(completion.getHabit()))
                entry.get(completion.getHabit()).add(completion);
        }

        cache.put(date, entry);
    }

    @Override
    public Map<Habit, Integer> getHabitsForDate(Date date) {
        return getHabitsForDate(simpleDateFormat.format(date), Weekday.fromDate(date, calendar));
    }

    private Map<Habit, Integer> getHabitsForDate(final String date, final Weekday weekday) {

        if (!cache.containsKey(date)) {
            datesRecorded.add(date);
            buildResultForDate(date, weekday);
        }

        final Map<Habit, Integer> result = new HashMap<Habit, Integer>();
        for (Map.Entry<Habit, List<Completion>> entry : cache.get(date).entrySet())
            result.put(entry.getKey(), entry.getValue().size());
        return result;
    }

    private void clearCache(Habit habit) {
        final Set<String> datesInvalid = new HashSet<>();

        for (String date : cache.keySet())
            if (cache.get(date).containsKey(habit))
                datesInvalid.add(date);

        cache.keySet().removeAll(datesInvalid);
    }

    private void clearCache(String date) {
        cache.keySet().remove(date);
    }

    @Override
    public void addHabit(Habit habit) {
        habits.add(habit);
        for (Weekday weekday : habit.getWeekdays())
            habitsInWeekday.get(weekday).add(habit);

        clearCache(habit);
        saveHabits();
    }

    @Override
    public void deleteHabit(Habit habit) {
        habits.remove(habit);
        for (Weekday weekday : habit.getWeekdays())
            habitsInWeekday.get(weekday).remove(habit);

        clearCache(habit);
        saveHabits();
    }

    @Override
    public void addCompletion(Habit habit, Date date) {
        final String formattedDate = simpleDateFormat.format(date);
        final Completion completion = new Completion(habit, formattedDate);
        completions.add(completion);

        clearCache(formattedDate);
        saveCompletions();
    }

    @Override
    public void deleteCompletion(Habit habit, Date date) {
        final String formattedDate = simpleDateFormat.format(date);
        final Completion completion = new Completion(habit, formattedDate);
        completions.remove(completion);

        clearCache(formattedDate);
        saveCompletions();
    }

    @Override
    public int timesHabitFulfilled(Habit habit) {
//        int timesFulfilled = 0;
//        for (String date : datesRecorded) {
//            final Integer completionsInDate = getHabitsForDate(date).get(habit);
//            if (completionsInDate != null && completionsInDate > 0)
//                timesFulfilled++;
//        }
//        return timesFulfilled;
        return 0;
    }

    @Override
    public int timesHabitUnfulfilled(Habit habit) {
//        int timesUnfulfilled = 0;
//        for (String date : datesRecorded) {
//            final Integer completionsInDate = getHabitsForDate(date).get(habit);
//            if (completionsInDate != null && completionsInDate == 0)
//                timesUnfulfilled++;
//        }
//        return timesUnfulfilled;
        return 0;
    }

    private void loadDataFromFile() {
        try {
            Gson gson = new Gson();

            File habitFile = context.getFileStreamPath(HABIT_FILENAME);
            if (habitFile != null && habitFile.exists()) {
                FileInputStream habitFis = context.openFileInput(HABIT_FILENAME);
                BufferedReader habitIn = new BufferedReader(new InputStreamReader(habitFis));
                Type habitType = new TypeToken<Set<Habit>>() {
                }.getType();
                habits.addAll(gson.<Set<Habit>>fromJson(habitIn, habitType));
            }

            File completionFile = context.getFileStreamPath(COMPLETION_FILENAME);
            if (completionFile != null && completionFile.exists()) {
                FileInputStream completionFis = context.openFileInput(COMPLETION_FILENAME);
                BufferedReader completionIn = new BufferedReader(new InputStreamReader(completionFis));
                Type completionType = new TypeToken<List<Completion>>() {
                }.getType();
                completions.addAll(gson.<List<Completion>>fromJson(completionIn, completionType));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void saveHabits() {
        try {
            FileOutputStream fos = context.openFileOutput(HABIT_FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
             throw new RuntimeException();
        } catch (IOException e) {
             throw new RuntimeException();
        }
    }

    @Override
    public void saveCompletions() {
        try {
            FileOutputStream fos = new FileOutputStream(COMPLETION_FILENAME, false);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(completions, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
