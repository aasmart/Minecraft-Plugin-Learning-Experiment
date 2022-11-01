package com.aasmart.PluginExperiment.entities.goals;

import com.aasmart.PluginExperiment.entities.types.AxolotlBombEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class AxolotlExplodeGoal extends Goal {
    private final AxolotlBombEntity axolotl;
    @Nullable
    private LivingEntity target;

    public AxolotlExplodeGoal(AxolotlBombEntity axolotl) {
        this.axolotl = axolotl;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.axolotl.getTarget();
        return livingEntity != null && this.axolotl.distanceToSqr(livingEntity) < 6.0D;
    }

    @Override
    public void start() {
        this.axolotl.getNavigation().stop();
        this.target = this.axolotl.getTarget();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if(this.target != null && this.axolotl.distanceToSqr(this.target) < 6.0D)
            this.axolotl.explode();
    }
}
