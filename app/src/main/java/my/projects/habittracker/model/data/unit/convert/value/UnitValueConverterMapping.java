package my.projects.habittracker.model.data.unit.convert.value;

import java.util.Objects;

import my.projects.habittracker.model.data.unit.Unit;

public class UnitValueConverterMapping {

    public final Unit from;
    public final Unit to;
    public final UnitValueConverter converter;

    public UnitValueConverterMapping(Unit from, Unit to, UnitValueConverter converter) {
        this.from = from;
        this.to = to;
        this.converter = converter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitValueConverterMapping that = (UnitValueConverterMapping) o;
        return from == that.from &&
                to == that.to;
    }

    @Override
    public int hashCode() {

        return Objects.hash(from, to);
    }
}
