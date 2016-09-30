package mendona.vitor.habittracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vitor on 29/09/16.
 */
public class HabitDate {

    final private Date innerDate;
    final private String stringRepresentation;
    final private Weekday weekday;

    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public HabitDate(Date date) {
        this.innerDate = date;
        this.stringRepresentation = sdf.format(date);
        this.weekday = Weekday.fromDate(date, Calendar.getInstance());
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
