package my.projects.habittracker.model.data.unit;

import java.util.Arrays;
import java.util.List;

public enum UnitSet {

    ABSTRACT(1, "abstract") {
        @Override
        public List<Unit> getUnits() {
            return Arrays.asList(Unit.TIMES);
        }
    }, TIME(2, "time") {
        @Override
        public List<Unit> getUnits() {
            return Arrays.asList(Unit.MINUTES, Unit.HOURS);
        }
    };

    private int id;
    private String setName;

    UnitSet(int id, String setName) {
        this.id = id;
        this.setName = setName;
    }

    public int getId() {
        return id;
    }

    public String getSetName() {
        return setName;
    }

    public abstract List<Unit> getUnits();

    public static List<UnitSet> getAll() {
        List<UnitSet> unitSets = Arrays.asList(values());
        return unitSets;
    }

    public String getUnitsExamplesString() {
        List<Unit> units = getUnits();
        String unitsText = "";
        for (Unit unit : units) {
            unitsText += "5 " + unit.getUnitName() + ", ";
        }
        return unitsText.substring(0, unitsText.length() - 2);
    }
}
