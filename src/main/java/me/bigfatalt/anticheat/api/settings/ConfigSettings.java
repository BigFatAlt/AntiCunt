package me.bigfatalt.anticheat.api.settings;

import me.bigfatalt.anticheat.AntiCunt;

public class ConfigSettings {

    public static String alertMessage = AntiCunt.instance.configUtil.getConfig().getString("alertMessage", "&5&lAntiCunt&d> &f%player &7violated &f%name &d(&5%vl&d)");

}
