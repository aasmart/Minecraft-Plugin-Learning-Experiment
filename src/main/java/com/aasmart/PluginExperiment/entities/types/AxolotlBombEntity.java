package com.aasmart.PluginExperiment.entities.types;

import com.aasmart.PluginExperiment.entities.ICustomEntity;
import com.aasmart.PluginExperiment.entities.goals.AxolotlEffectsGoal;
import com.aasmart.PluginExperiment.entities.goals.AxolotlExplodeGoal;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;

public class AxolotlBombEntity extends Axolotl implements ICustomEntity {
    public static final String MOB_ID = "AxolotlBomb";

    public AxolotlBombEntity(Location location) {
        super(EntityType.AXOLOTL, ((CraftWorld) location.getWorld()).getHandle());
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCustomName(new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "Axolotl Friend"));
        this.setCustomNameVisible(true);

        this.goalSelector.removeAllGoals();
        this.targetSelector.removeAllGoals();
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false,
                livingEntity -> livingEntity.getClass() != Axolotl.class && livingEntity.getClass() != AxolotlBombEntity.class));
        this.goalSelector.addGoal(0, new AxolotlExplodeGoal(this));
        this.goalSelector.addGoal(1, new AxolotlEffectsGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    public void explode() {
        CraftLivingEntity axolotl = this.getBukkitLivingEntity();

        World world = axolotl.getWorld();
        Location loc = axolotl.getLocation();
        world.createExplosion(loc, 10, true, true, axolotl);
        world.spawnParticle(Particle.FLAME, loc, 500, 0, 0, 0, .3);
        world.spawnParticle(Particle.SMOKE_LARGE, loc, 100, 0, 0, 0, .4);

        this.kill();
    }

    public void effects() {
        CraftLivingEntity axolotl = this.getBukkitLivingEntity();
        axolotl.getWorld().spawnParticle(Particle.LAVA, axolotl.getEyeLocation(), 1);
        axolotl.getWorld().playSound(axolotl.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 3, 1);
    }

    /*
    IDEA: USE REFLECTION FOR MOB SPAWNING
    */
}
