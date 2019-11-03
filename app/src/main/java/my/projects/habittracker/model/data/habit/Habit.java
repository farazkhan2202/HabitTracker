package my.projects.habittracker.model.data.habit;

import java.util.Date;
import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import my.projects.habittracker.model.data.unit.UnitSet;
import my.projects.habittracker.model.data.unit.convert.db.UnitTypesConverter;

@Entity(tableName = "habits")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;

    @ColumnInfo(name = "last_updated")
    private long lastUpdatedMillis;

    @ColumnInfo(name = "unit_set_id")
    @TypeConverters({UnitTypesConverter.class})
    private UnitSet unitSet;

    public Habit(int id, String title, String description, UnitSet unitSet, long lastUpdatedMillis) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.unitSet = unitSet;
        this.lastUpdatedMillis = lastUpdatedMillis;
    }

    @Ignore
    public Habit(String title, String description, UnitSet unitSet) {
        this.title = title;
        this.description = description;
        this.unitSet = unitSet;
        lastUpdatedMillis = new Date().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnitSet getUnitSet() {
        return unitSet;
    }

    public void setUnitSet(UnitSet unitSet) {
        this.unitSet = unitSet;
    }

    public long getLastUpdatedMillis() {
        return lastUpdatedMillis;
    }

    public void setLastUpdatedMillis(long lastUpdatedMillis) {
        this.lastUpdatedMillis = lastUpdatedMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return id == habit.id &&
                Objects.equals(title, habit.title) &&
                Objects.equals(description, habit.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, description);
    }
}
