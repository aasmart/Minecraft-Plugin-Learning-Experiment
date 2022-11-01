package com.aasmart.PluginExperiment.entities.goals;

import com.aasmart.PluginExperiment.entities.types.AxolotlBombEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class AxolotlEffectsGoal extends Goal {
    private final AxolotlBombEntity axolotl;
    @Nullable
    private LivingEntity target;

    public AxolotlEffectsGoal(AxolotlBombEntity axolotl) {
        this.axolotl = axolotl;
        this.setFlags(EnumSet.of(Flag.UNKNOWN_BEHAVIOR));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.axolotl.getTarget();
        return livingEntity != null;
    }

    @Override
    public void start() {
        this.target = this.axolotl.getTarget();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if(target != null)
            axolotl.effects();
    }
}
