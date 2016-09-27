package mendona.vitor.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vitor on 26/09/16.
 */
public class HabitsOnScreenAdapter extends ArrayAdapter<HabitOnScreen> {

    public HabitsOnScreenAdapter(Context context, ArrayList<HabitOnScreen> habitsOnScreen) {
        super(context, 0, habitsOnScreen);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewToUse;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            viewToUse = layoutInflater.inflate(R.layout.habit_on_screen, parent, false);
        } else {
            viewToUse = convertView;
        }

        final HabitOnScreen habitOnScreen = getItem(position);
        final TextView habitName = (TextView) viewToUse.findViewById(R.id.habit_name);
        habitName.setText(habitOnScreen.getHabit().getName());
        final TextView completionCount = (TextView) viewToUse.findViewById(R.id.number_of_completions);
        completionCount.setText(String.valueOf(habitOnScreen.getCompletionCount()));

        return viewToUse;
    }
}
