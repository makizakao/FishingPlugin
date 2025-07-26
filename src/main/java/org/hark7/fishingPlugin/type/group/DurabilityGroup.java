package org.hark7.fishingPlugin.type.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hark7.fishingPlugin.type.item.Fishable;

@SuperBuilder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DurabilityGroup {
    @Getter
    private int minLevel;
    @Getter
    private int minRepair;
    @Getter
    private int maxRepair;

    public abstract static class DurabilityGroupBuilder<C extends DurabilityGroup, B extends DurabilityGroupBuilder<C, B>> {
        protected int minLevel;
        protected int minRepair;
        protected int maxRepair;

        public B minLevel(int minLevel) {
            if (minLevel < 1) throw new IllegalArgumentException("Minimum level must be at least 1.");
            this.minLevel = minLevel;
            return self();
        }

        public B minRepair(int minRepair) {
            if (minRepair < 0) throw new IllegalArgumentException("Minimum repair must be at least 0.");
            this.minRepair = minRepair;
            return self();
        }

        public B maxRepair(int maxRepair) {
            if (maxRepair < 0) throw new IllegalArgumentException("Maximum repair must be at least 0.");
            this.maxRepair = maxRepair;
            return self();
        }
    }
}
