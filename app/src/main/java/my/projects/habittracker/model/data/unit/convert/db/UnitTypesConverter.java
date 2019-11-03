package my.projects.habittracker.model.data.unit.convert.db;

import androidx.room.TypeConverter;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.UnitSet;

public class UnitTypesConverter {

    @TypeConverter
    public static Unit toUnitFromId(int id) {
        Unit[] units = Unit.values();
        for (int i = 0; i < units.length; i++) {
            if (units[i].getId() == id) {
                return units[i];
            }
        }
        return null;
    }

    @TypeConverter
    public static int toIdFromUnit(Unit unit) {
        return unit.getId();
    }

    @TypeConverter
    public static UnitSet toUnitSetFromId(int id) {
        UnitSet[] unitSets = UnitSet.values();
        for (int i = 0; i < unitSets.length; i++) {
            if (unitSets[i].getId() == id) {
                return unitSets[i];
            }
        }
        return null;
    }

    @TypeConverter
    public static int toIdFromUnitSet(UnitSet unitSet) {
        return unitSet.getId();
    }

}
