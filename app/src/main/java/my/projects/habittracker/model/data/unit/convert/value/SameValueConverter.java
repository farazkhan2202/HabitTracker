package my.projects.habittracker.model.data.unit.convert.value;

public class SameValueConverter implements UnitValueConverter {
    @Override
    public double convert(double value) {
        return value;
    }
}
