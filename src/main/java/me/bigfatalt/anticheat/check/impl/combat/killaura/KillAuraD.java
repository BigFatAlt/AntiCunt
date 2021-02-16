package me.bigfatalt.anticheat.check.impl.combat.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.data.PlayerData;

@CheckType(label = "KillAura D", category = Category.Combat)
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class KillAuraD extends Check {

    private int swings, vl;

    public KillAuraD(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) object;
            if (useEntityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                if (swings++ > 2) {
                    if (vl++ > 3) {
                        flag("swings+= " + swings);
                    }
                } else if (vl > 0) vl--;
            }
        }

        if (object instanceof WrappedInArmAnimationPacket) {
            swings= 0;
        }
    }
}
