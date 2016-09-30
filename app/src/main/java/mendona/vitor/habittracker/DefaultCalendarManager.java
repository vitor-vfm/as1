package mendona.vitor.habittracker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by vitor on 15/09/16.
 */
public class DefaultCalendarManager implements CalendarManager {

    private final String habitFilename;
    private final String completionFilename;

    private final List<Completion> completions;
    private final Set<Habit> habits;
    private final Set<HabitDate> datesRecorded;

    private final Map<Weekday, Set<Habit>> habitsInWeekday;
    private final Context context;

    private final Map<HabitDate, Map<Habit, List<Completion>>> cache;

    public DefaultCalendarManager(final Context context,
                                  final Date currentDate,
                                  final String habitFilename,
                                  final String completionFilename) {

        this.completions = new ArrayList<Completion>();
        this.habits = new HashSet<Habit>();
        this.datesRecorded = new HashSet<HabitDate>();
        this.cache = new HashMap<HabitDate, Map<Habit, List<Completion>>>();
        this.context = context;

        if (null == habitFilename || null == completionFilename)
            throw new IllegalArgumentException("Filenames cannot be null");

        this.habitFilename = habitFilename;
        this.completionFilename = completionFilename;

        loadDataFromHabitsFile();
        loadDataFromCompletionsFile();

        this.habitsInWeekday = new HashMap<Weekday, Set<Habit>>();
        for (Weekday weekday : Weekday.values())
            habitsInWeekday.put(weekday, new HashSet<Habit>());
        for (Habit habit : habits)
            for (Weekday weekday : habit.getWeekdays())
                habitsInWeekday.get(weekday).add(habit);

        datesRecorded.add(new HabitDate(currentDate));

    }

    private void buildResultForDate(final HabitDate date) {
        final Map<Habit, List<Completion>> entry = new HashMap<Habit, List<Completion>>();

        for (Habit habit : habitsInWeekday.get(date.getWeekday())) {
            entry.put(habit, new ArrayList<Completion>());
        }

        for (Completion completion : completions) {
            if (entry.containsKey(completion.getHabit()) && completion.getDate().equals(date))
                entry.get(completion.getHabit()).add(completion);
        }

        cache.put(date, entry);
    }

    @Override
    public Map<Habit, List<Completion>> getHabitsForDate(Date date) {
        return getHabitsForDate(new HabitDate(date));
    }

    private Map<Habit, List<Completion>> getHabitsForDate(final HabitDate date) {

        if (!cache.containsKey(date)) {
            datesRecorded.add(date);
            buildResultForDate(date);
        }

        return cache.get(date);
    }


    private void clearCache(HabitDate date) {
        cache.keySet().remove(date);
    }

    @Override
    public Set<Habit> getAllHabits() {
        // return a copy to avoid having the inner set mutated
        return new HashSet<>(habits);
    }

    @Override
    public Habit getHabitByName(String habitName) {

        for (Habit habit: habits) {
            if (habit.getName().equals(habitName)) {
                return habit;
            }
        }

        return null;
    }

    @Override
    public List<Completion> getCompletionsForHabit(String habitName) {
        final List<Completion> result = new ArrayList<>();
        for (Completion completion : completions) {
            if (completion.getHabit().getName().equals(habitName))
                result.add(completion);
        }
        return result;
    }

    private boolean validNewHabit(Habit habit) {
        if (habit == null || habit.getName() == null || habit.getName().isEmpty() || habits.contains(habit))
            return false;

        for (Habit existingHabit : habits)
            if (existingHabit.getName().equals(habit.getName()))
                return false;

        return true;
    }

    @Override
    public boolean addHabit(Habit habit) {
        if (!validNewHabit(habit))
            return false;

        habits.add(habit);
        for (Weekday weekday : habit.getWeekdays())
            habitsInWeekday.get(weekday).add(habit);

        cache.clear();
        saveHabits();
        return true;
    }

    @Override
    public void deleteHabit(Habit habit) {
        habits.remove(habit);

        for (Weekday weekday : habit.getWeekdays()) {
            habitsInWeekday.get(weekday).remove(habit);
        }

        final List<Completion> previousCompletions = new ArrayList<>(completions);
        completions.clear();
        for (Completion completion : previousCompletions) {
            if (!completion.getHabit().equals(habit))
                completions.add(completion);
        }

        cache.clear();
        saveHabits();
        saveCompletions();
    }

    private boolean validNewCompletion(Completion completion) {
        return habits.contains(completion.getHabit());
    }

    @Override
    public boolean addCompletion(Habit habit, Date date) {
        final Completion completion = new Completion(habit, date);
        if (!validNewCompletion(completion))
            return false;
        completions.add(completion);

        clearCache(new HabitDate(date));
        saveCompletions();
        return true;
    }

    @Override
    public void deleteCompletion(Completion completion) {
        completions.remove(completion);

        clearCache(completion.getDate());
        saveCompletions();
    }

    @Override
    public int timesHabitFulfilled(Habit habit, Date currentDate) {

        final Set<Weekday> habitWeekdays = habit.getWeekdays();
        final HabitDate originalDate = habit.getOriginalDate();
        final Comparator<HabitDate> dateComparator = new Comparator<HabitDate>() {
            @Override
            public int compare(HabitDate lhs, HabitDate rhs) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(lhs.getJavaDate());
                int lYear = calendar.get(Calendar.YEAR);
                int lMonth = calendar.get(Calendar.MONTH);
                int lDay = calendar.get(Calendar.DAY_OF_MONTH);

                calendar.setTime(rhs.getJavaDate());
                int rYear = calendar.get(Calendar.YEAR);
                int rMonth = calendar.get(Calendar.MONTH);
                int rDay = calendar.get(Calendar.DAY_OF_MONTH);

                if (lYear < rYear) return 1;
                else if (lYear > rYear) return -1;
                else if (lMonth < rMonth) return 1;
                else if (lMonth > rMonth) return -1;
                else if (lDay == rDay) return 0;
                else return lDay > rDay ? -1 : 1;
            }
        };

        final Set<HabitDate> shouldBeCompletedIn = new HashSet<>();
        for (HabitDate date : datesRecorded) {
            if (habitWeekdays.contains(date.getWeekday()) && dateComparator.compare(originalDate, date) >= 0)
                shouldBeCompletedIn.add(date);
        }

        final Set<HabitDate> wasCompletedIn = new HashSet<>();
        for (Completion completion : completions) {
            if (completion.getHabit().equals(habit))
                wasCompletedIn.add(completion.getDate());
        }

        shouldBeCompletedIn.retainAll(wasCompletedIn);
        return shouldBeCompletedIn.size();
    }

    private void createDataFile(String filePath) {
        try {
            final FileOutputStream fos = context.openFileOutput(filePath, 0);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromHabitsFile() {
        Gson gson = new Gson();

        try {
            FileInputStream habitFis = context.openFileInput(habitFilename);
            BufferedReader habitIn = new BufferedReader(new InputStreamReader(habitFis));
            Type habitType = new TypeToken<Set<Habit>>(){}.getType();
            final Set<Habit> habitsFromFile = gson.<Set<Habit>>fromJson(habitIn, habitType);
            if (habitsFromFile != null) {
                habits.addAll(habitsFromFile);
            }
        } catch (FileNotFoundException fnfe) {
            createDataFile(habitFilename);
        } catch (RuntimeException re) {
            context.deleteFile(habitFilename);
            throw new RuntimeException("File " + habitFilename + " was corrupted and was deleted");
        }
    }

    private void loadDataFromCompletionsFile() {
        Gson gson = new Gson();
        try {
            FileInputStream completionFis = context.openFileInput(completionFilename);
            BufferedReader completionIn = new BufferedReader(new InputStreamReader(completionFis));
            Type completionType = new TypeToken<List<Completion>>() {
            }.getType();
            List<Completion> completionsFromFile = gson.<List<Completion>>fromJson(completionIn, completionType);
            if (completionsFromFile != null) {
                completions.addAll(completionsFromFile);
                for (Completion completion : completions)
                    datesRecorded.add(completion.getDate());
            }
        } catch (FileNotFoundException fnfe) {
            createDataFile(completionFilename);
        } catch (RuntimeException re) {
            context.deleteFile(completionFilename);
            throw new RuntimeException("File " + completionFilename + " was corrupted and was deleted");
        }
    }

    private void saveHabits() {
        try {
            FileOutputStream fos = context.openFileOutput(habitFilename, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
             throw new RuntimeException("Could not open habits persistence file");
        } catch (IOException e) {
             throw new RuntimeException();
        }
    }

    private void saveCompletions() {
        try {
            FileOutputStream fos = context.openFileOutput(completionFilename, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(completions, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not open completions persistence file");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
