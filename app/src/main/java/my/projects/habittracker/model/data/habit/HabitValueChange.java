package my.projects.habittracker.model.data.habit;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.convert.db.UnitTypesConverter;

@Entity(tableName = "changes")
public class HabitValueChange {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "habit_id")
    private long habitId;

    @ColumnInfo(name = "time")
    private long timeInMillis;

    @ColumnInfo(name = "unit_id")
    @TypeConverters({UnitTypesConverter.class})
    private Unit unit;

    private double delta;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HabitValueChange change = (HabitValueChange) o;
        return id == change.id &&
                habitId == change.habitId &&
                timeInMillis == change.timeInMillis &&
                delta == change.delta &&
                unit.getId() == unit.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, habitId, timeInMillis, delta, unit.getId());
    }

    @NonNull
    @Override
    public String toString() {
        String datetimeString = timeInMillisToString();
        return "+" + String.format(Locale.getDefault(), "%.1f", delta) + " " + unit.getUnitName() + " on " + datetimeString;
    }

    private String timeInMillisToString() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date datetime = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(datetime);
    }
}
