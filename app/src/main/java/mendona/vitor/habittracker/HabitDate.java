package mendona.vitor.habittracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vitor on 29/09/16.
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
