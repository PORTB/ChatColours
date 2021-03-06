package portb.chatcolours.mixin;

import net.minecraft.util.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.chatcolours.ServerConfig;

@Mixin(SharedConstants.class)
public class SharedConstantsMixins
{
    @Inject(method = "Lnet/minecraft/util/SharedConstants;isAllowedCharacter(C)Z", at = @At("RETURN"), cancellable = true)
    private static void isAllowedCharacter(char c, CallbackInfoReturnable<Boolean> returnInfo)
    {
        if(ServerConfig.isEnabled.get())
        {
            returnInfo.setReturnValue(c >= ' ' && c != 127);
            returnInfo.cancel();
        }
    }
}
