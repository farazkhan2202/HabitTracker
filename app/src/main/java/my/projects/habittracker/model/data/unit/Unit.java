package my.projects.habittracker.model.data.unit;

import android.util.Log;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import my.projects.habittracker.model.data.unit.convert.value.UnitValueConverterFactory;

public enum Unit {

    TIMES(1, "times") {
        @Override
        public UnitSet getUnitSet() {
            return UnitSet.ABSTRACT;
        }
    }, MINUTES(2, "minutes") {
        @Override
        public UnitSet getUnitSet() {
            return UnitSet.TIME;
        }
    }, HOURS(3, "hours") {
        @Override
        public UnitSet getUnitSet() {
            return UnitSet.TIME;
        }
    };

    private UnitValueConverterFactory converterFactory = new UnitValueConverterFactory(this);

    private int id;
    private String unitName;

    Unit(int id, String unitName) {
        this.id = id;
        this.unitName = unitName;
    }

    public int getId() {
        return id;
    }

    public String getUnitName() {
        return unitName;
    }

    public abstract UnitSet getUnitSet();

    public double convertValueTo(double value, Unit unit) {
        return converterFactory.getConverterTo(unit).convert(value);
    }
}
