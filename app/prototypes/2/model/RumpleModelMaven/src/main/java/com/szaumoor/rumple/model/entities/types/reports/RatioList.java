package com.szaumoor.rumple.model.entities.types.reports;

import com.szaumoor.rumple.model.interfaces.Entity;

import java.util.List;

public abstract class RatioList<I extends Entity> extends StatList<RatioList.Ratio, I>  {
    protected RatioList(List<I> items) {
        super(items);
    }

    public record Ratio(String name, double ratio) implements Stat {
        public double asPercentage() {
            return ratio * 100.0;
        }
        public String percentageAsString() {
            return asPercentage() + "%";
        }
        public String ratioAsString() {
            return ratio + "%";
        }
    }
}
