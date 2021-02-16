package me.bigfatalt.anticheat.data.manager;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import me.bigfatalt.anticheat.AntiCunt;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.check.impl.combat.aim.*;
import me.bigfatalt.anticheat.check.impl.combat.autoclicker.*;
import me.bigfatalt.anticheat.check.impl.combat.killaura.*;
import me.bigfatalt.anticheat.check.impl.movement.fly.FlyA;
import me.bigfatalt.anticheat.check.impl.movement.fly.FlyB;
import me.bigfatalt.anticheat.check.impl.other.badpackets.*;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CheckManager {

    private final ClassToInstanceMap<Check> checks;

    public CheckManager(final PlayerData playerData) {
        checks = new ImmutableClassToInstanceMap.Builder<Check>()
                .put(AimA.class, new AimA(playerData))
                .put(AimB.class, new AimB(playerData))
                .put(AimC.class, new AimC(playerData))
                .put(AimD.class, new AimD(playerData))
                .put(AimE.class, new AimE(playerData))
                .put(AimF.class, new AimF(playerData))
                .put(AimG.class, new AimG(playerData))
                .put(AimH.class, new AimH(playerData))
                .put(AimI.class, new AimI(playerData))
                .put(AimJ.class, new AimJ(playerData))
                .put(AimK.class, new AimK(playerData))
                .put(TestAim.class, new TestAim(playerData))
                .put(AutoClickerA.class, new AutoClickerA(playerData))
                .put(AutoClickerB.class, new AutoClickerB(playerData))
                .put(AutoClickerC.class, new AutoClickerC(playerData))
                .put(AutoClickerD.class, new AutoClickerD(playerData))
                .put(BadPacketsA.class, new BadPacketsA(playerData))
                .put(BadPacketsC.class, new BadPacketsC(playerData))
                .put(BadPacketsD.class, new BadPacketsD(playerData))
                .put(BadPacketsE.class, new BadPacketsE(playerData))
                .put(FlyA.class, new FlyA(playerData))
                .put(FlyB.class, new FlyB(playerData))
                .put(KillAuraA.class, new KillAuraA(playerData))
                .put(KillAuraB.class, new KillAuraB(playerData))
                .put(KillAuraC.class, new KillAuraC(playerData))
                .put(KillAuraD.class, new KillAuraD(playerData))
                .build();

    }


    public Collection<Check> getChecks() {
        return checks.values();
    }

    public Check getCheck(String name) {
        return checks.values().stream().filter(check -> check.label.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}