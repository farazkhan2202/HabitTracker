package my.projects.habittracker.model.data.unit.convert.value;

public class MinutesToHoursConverter implements UnitValueConverter {

    @Override
    public double convert(double value) {
        return value / 60;
    }
}
