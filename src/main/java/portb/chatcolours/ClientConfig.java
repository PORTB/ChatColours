package portb.chatcolours;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue isEnabled;
    public static final ForgeConfigSpec.BooleanValue isExtendedSyntaxEnabled;

    static
    {
        builder.comment("Note: If you are on a sever it needs to have this mod installed, or otherwise allow you to send messages with '" + '\u00A7' + "' in them.");
        isEnabled = builder.comment("Enable/disable typing " + '\u00A7' + "in text fields").define("Enabled", true);
        isExtendedSyntaxEnabled = builder.comment("Enable/disable using extended syntax, such as " + '\u00A7' + "[blue][u]hello").define("Enable extended syntax", true);

        SPEC = builder.build();
    }
}
