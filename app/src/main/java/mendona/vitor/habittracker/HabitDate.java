package mendona.vitor.habittracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A custom date object. It wraps a regular Date with it corresponding day of the week
 * and a string representation. Any time a date is used by the application model, this
 * should be used.
 *
 * It's meant to be immutable. Getters should return copies to avoid mutation of its fields.
 */
public class HabitDate {

    static String FORMAT = "yyyy-MM-dd";

    final private Date innerDate;
    final private String stringRepresentation;
    final private Weekday weekday;

    public HabitDate(Date date) {
        this.innerDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT, Locale.getDefault());
        this.stringRepresentation = sdf.format(date);
        this.weekday = Weekday.fromDate(date);
    }

    public Date getJavaDate() {
        return (Date) innerDate.clone();
    }

    public Weekday getWeekday() {
        return weekday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HabitDate habitDate = (HabitDate) o;

        return stringRepresentation != null ? stringRepresentation.equals(habitDate.stringRepresentation) : habitDate.stringRepresentation == null;

    }

    @Override
    public int hashCode() {
        return stringRepresentation != null ? stringRepresentation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
