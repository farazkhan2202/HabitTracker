package my.projects.habittracker.model.data.unit.convert.value;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import my.projects.habittracker.model.data.unit.Unit;

import static my.projects.habittracker.model.data.unit.Unit.HOURS;
import static my.projects.habittracker.model.data.unit.Unit.MINUTES;
import static my.projects.habittracker.model.data.unit.Unit.TIMES;

public class UnitValueConverterFactory {

    private static Set<UnitValueConverterMapping> mappings;

    private static void init() {
        mappings = new HashSet<>();

        UnitValueConverterMapping minutesToHours = new UnitValueConverterMapping(MINUTES, HOURS, new MinutesToHoursConverter());
        UnitValueConverterMapping hoursToMinutes = new UnitValueConverterMapping(HOURS, MINUTES, new HoursToMinutesConverter());

        mappings.add(minutesToHours);
        mappings.add(hoursToMinutes);
    }

    private Unit baseUnit;

    public UnitValueConverterFactory(Unit baseUnit) {
        this.baseUnit = baseUnit;
    }

    public UnitValueConverter getConverterTo(Unit unit) {
        if (mappings == null) {
            init();
        }

        if (unit.equals(baseUnit)) {
            return new SameValueConverter();
        }
        for (UnitValueConverterMapping mapping : mappings) {
            if (mapping.from.equals(baseUnit) && mapping.to.equals(unit)) {
                return mapping.converter;
            }
        }
        throw new IllegalArgumentException("Conversion of this type is not defined: " + baseUnit.getUnitName() + " to " + unit.getUnitName());
    }

}
