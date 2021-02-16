package me.bigfatalt.anticheat.check.impl.combat.autoclicker;

import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.objects.evicting.EvictingList;
import lombok.val;
import me.bigfatalt.anticheat.api.check.Category;
import me.bigfatalt.anticheat.api.check.CheckType;
import me.bigfatalt.anticheat.api.check.Punishment;
import me.bigfatalt.anticheat.check.api.Check;
import me.bigfatalt.anticheat.data.PlayerData;
import me.bigfatalt.anticheat.utils.MathUtil;

@CheckType(label = "AutoClicker E", category = Category.Combat)
@Punishment
public class AutoClickerE extends Check {

    private int movement;
    private EvictingList<Integer> evictingList = new EvictingList<>(50);

    public AutoClickerE(PlayerData data) {
        super(data);
    }

    @Override
    public void handleCheck(Object object, long timeStamp) {
        if (object instanceof WrappedInArmAnimationPacket) {
            if (movement < 5) {
                evictingList.add(movement);


                if (evictingList.size() == evictingList.getMaxSize()) {
                    double kurtosis = MathUtil.getKurtosis(evictingList);

                    debug(kurtosis + "");
                    evictingList.clear();
                }

                movement = 0;
            }
        }

        if (object instanceof WrappedInFlyingPacket) {
            movement++;
        }
    }
}
