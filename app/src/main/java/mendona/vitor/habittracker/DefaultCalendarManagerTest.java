package mendona.vitor.habittracker;

import junit.framework.TestCase;
import junit.framework.TestResult;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vitor on 29/09/16.
 */
public class DefaultCalendarManagerTest extends TestCase {

    final Set<Weekday> testWeekdays1 = new HashSet<>(Arrays.asList(Weekday.FRIDAY, Weekday.MONDAY, Weekday.THURSDAY));

    public void testAddHabit(){
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        assertTrue(dcm.addHabit(new Habit("test1", Calendar.getInstance().getTime(), testWeekdays1)));
    }

    public void testDeleteHabit(){
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Habit testHabit = new Habit("test1", Calendar.getInstance().getTime(), testWeekdays1);
        dcm.addHabit(testHabit);
        dcm.deleteHabit(testHabit);
        assertTrue(dcm.getAllHabits().isEmpty());
    }

    public void testAddCompletion(){
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Date currentDate = Calendar.getInstance().getTime();
        final Habit testHabit = new Habit("test1", currentDate, testWeekdays1);
        final Completion testCompletion = new Completion(testHabit, currentDate);
        dcm.addHabit(testHabit);
        dcm.addCompletion(testHabit, currentDate);
        List<Completion> actual = dcm.getCompletionsForHabit(testHabit.getName());
        assertTrue(actual.size() == 1);
        assertTrue(actual.contains(testCompletion));
    }

    public void testDeleteCompletion(){
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Date currentDate = Calendar.getInstance().getTime();
        final Habit testHabit = new Habit("test1", currentDate, testWeekdays1);
        final Completion testCompletion = new Completion(testHabit, currentDate);
        dcm.addCompletion(testHabit, currentDate);
        dcm.deleteCompletion(testCompletion);
        assertTrue(dcm.getCompletionsForHabit(testHabit.getName()).isEmpty());
    }

    public void testGetHabitsForDate() {
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Date currentDate = Calendar.getInstance().getTime();
        final Habit testHabit = new Habit("test1", currentDate, testWeekdays1);
        final Completion testCompletion = new Completion(testHabit, currentDate);
        dcm.addHabit(testHabit);
        dcm.addCompletion(testHabit, currentDate);
        final Map<Habit, List<Completion>> actual = dcm.getHabitsForDate(currentDate);
        assertTrue(actual.size() == 1);
        final List<Completion> list = actual.get(testHabit);
        assertTrue(list != null);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0).equals(testCompletion));
    }

    public void testGetAllHabits(){
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Set<Habit> shouldBeEmpty = dcm.getAllHabits();
        assertTrue(shouldBeEmpty != null && shouldBeEmpty.isEmpty());
        final Set<Habit> expected = new HashSet<>();

        expected.add(new Habit("test1", Calendar.getInstance().getTime(), testWeekdays1));
        expected.add(new Habit("test2", Calendar.getInstance().getTime(), testWeekdays1));
        expected.add(new Habit("test3", Calendar.getInstance().getTime(), testWeekdays1));

        for (Habit habit : expected) {
            dcm.addHabit(habit);
        }

        final Set<Habit> actual = dcm.getAllHabits();
        assertTrue(actual.size() == expected.size());
        for (Habit habit : actual) {
            assertTrue(expected.contains(habit));
        }
    }

    public void testGetHabitByName() {
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Habit testHabit = new Habit("test1", Calendar.getInstance().getTime(), testWeekdays1);
        dcm.addHabit(testHabit);
        assertTrue(testHabit.equals(dcm.getHabitByName("test1")));
    }

    public void testtimesHabitFulfilled(){
        final MockDefaultCalendarManager dcm = new MockDefaultCalendarManager();
        final Date currentDate = Calendar.getInstance().getTime();
        final Habit testHabit = new Habit("test1", currentDate, testWeekdays1);
        dcm.addHabit(testHabit);
        dcm.addCompletion(testHabit, currentDate);
        assertTrue(dcm.timesHabitFulfilled(testHabit, Calendar.getInstance().getTime()) == 1);
    }
}
