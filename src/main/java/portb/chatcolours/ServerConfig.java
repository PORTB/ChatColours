package portb.chatcolours;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue isEnabled;
    //public static final ForgeConfigSpec.BooleanValue isFormattingEnabled;
    //public static final ForgeConfigSpec.BooleanValue areColoursEnabled;
    //public static final ForgeConfigSpec.BooleanValue isObfuscationEnabled;

    static
    {
        builder.comment("These settings control what players connected to a server can do.");

        isEnabled = builder.comment("Enable/disable players using formatting/colours.").define("Enabled", true);
        //areColoursEnabled = builder.comment("Enable/disable players using colours").define("Enable colours", true);
        //isObfuscationEnabled = builder.comment("Enable/disable players using obfuscation, as it can be used obnoxiously").define("Enable obfuscation", true);

        SPEC = builder.build();
    }
}
