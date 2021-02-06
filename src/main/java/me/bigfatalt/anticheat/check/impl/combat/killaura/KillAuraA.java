package me.bigfatalt.anticheat.check.impl.combat.killaura;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.utils.MathUtils;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.PunishmentType;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "KillAura A")
@Punishment(autoban = true, punishment = PunishmentType.BAN)
public class KillAuraA extends Check {

    private int vl;
    private long lastFlying;

    public KillAuraA(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) object;
            if (useEntityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                long post = System.currentTimeMillis() - lastFlying;
                if (post < 1) {
                    if (vl++ > 5) {
                        flag("Post:" + post);
                        punish("KillAura");
                    }
                } else if (vl > 0) vl--;

                debug("post+= " + post + " lagging+= " + playerData.misc.lagging);

            }
        }
        if (object instanceof WrappedInFlyingPacket) {
            this.lastFlying = System.currentTimeMillis();
        }
    }
}
