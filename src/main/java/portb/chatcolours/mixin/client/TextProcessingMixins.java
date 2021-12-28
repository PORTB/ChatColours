package portb.chatcolours.mixin.client;

import net.minecraft.entity.ai.goal.InteractDoorGoal;
import net.minecraft.util.ICharacterConsumer;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextProcessing;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.chatcolours.ClientConfig;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(TextProcessing.class)
public class TextProcessingMixins
{
    private static final Map<String, Character> FORMAT_CODES = new HashMap<>();
    private static final int LONGEST_FORMAT_CODE_LENGTH;

    static
    {
        //colours that don't match the dark/light pattern
        FORMAT_CODES.put("black", '0');
        FORMAT_CODES.put("white", 'f');
        FORMAT_CODES.put("blood", 'c');
        FORMAT_CODES.put("orange", '6');
        FORMAT_CODES.put("gold", '6');

        //generate variations of the colour names
        generateNamesForColour("blue", '1', '9');
        generateNamesForColour("green", '2', 'a');
        generateNamesForColour("aqua", '3', 'b');
        generateNamesForColour("cyan", '3', 'b'); //alternative name for aqua
        generateNamesForColour("red", '4', 'c');
        generateNamesForColour("purple", '5', 'd');
        generateNamesForColour("gray", '8', '7');
        generateNamesForColour("yellow", '6', 'e');

        //other formatting
        FORMAT_CODES.put("bold", 'l');
        FORMAT_CODES.put("strong", 'l');
        FORMAT_CODES.put("heavy", 'l');
        FORMAT_CODES.put("b", 'l');

        FORMAT_CODES.put("strikeout", 'm');
        FORMAT_CODES.put("strikethrough", 'm');
        FORMAT_CODES.put("strike", 'm');
        FORMAT_CODES.put("s", 'm');

        FORMAT_CODES.put("u", 'n');
        FORMAT_CODES.put("underline", 'n');

        FORMAT_CODES.put("i", 'o');
        FORMAT_CODES.put("italic", 'o');
        FORMAT_CODES.put("slanted", 'o');
        FORMAT_CODES.put("slant", 'o');

        FORMAT_CODES.put("obfuscated", 'k');
        FORMAT_CODES.put("o", 'k');

        FORMAT_CODES.put("reset", 'r');
        FORMAT_CODES.put("clear", 'r');
        FORMAT_CODES.put("default", 'r');
        FORMAT_CODES.put("r", 'r');

        LONGEST_FORMAT_CODE_LENGTH = findLongestFormatCode();
    }

    private static void generateNamesForColour(String colourName, char darkCode, char lightCode)
    {
        FORMAT_CODES.put("deep " + colourName, darkCode);
        FORMAT_CODES.put("deep" + colourName, darkCode);
        FORMAT_CODES.put("deep_" + colourName, darkCode);

        FORMAT_CODES.put("dark " + colourName, darkCode);
        FORMAT_CODES.put("dark" + colourName, darkCode);
        FORMAT_CODES.put("dark_" + colourName, darkCode);

        FORMAT_CODES.put("d_" + colourName, darkCode);
        FORMAT_CODES.put("d" + colourName, darkCode);

        FORMAT_CODES.put(colourName + " dark", darkCode);
        FORMAT_CODES.put(colourName + "_dark", darkCode);
        FORMAT_CODES.put(colourName + "dark", darkCode);

        FORMAT_CODES.put(colourName + " deep", darkCode);
        FORMAT_CODES.put(colourName + "_deep", darkCode);
        FORMAT_CODES.put(colourName + "deep", darkCode);

        FORMAT_CODES.put(colourName + "_d", darkCode);
        FORMAT_CODES.put(colourName + "d", darkCode);

        FORMAT_CODES.put(colourName, lightCode);

        FORMAT_CODES.put("light_" + colourName, lightCode);
        FORMAT_CODES.put("light " + colourName, lightCode);
        FORMAT_CODES.put("light" + colourName, lightCode);

        FORMAT_CODES.put("bright_" + colourName, lightCode);
        FORMAT_CODES.put("bright " + colourName, lightCode);
        FORMAT_CODES.put("bright" + colourName, lightCode);

        FORMAT_CODES.put(colourName + "_light", lightCode);
        FORMAT_CODES.put(colourName + " light", lightCode);
        FORMAT_CODES.put(colourName + "light", lightCode);

        FORMAT_CODES.put(colourName + "_bright", lightCode);
        FORMAT_CODES.put(colourName + " bright", lightCode);
        FORMAT_CODES.put(colourName + "bright", lightCode);
    }

    private static int findLongestFormatCode()
    {
        int max = 0;

        for (String key: FORMAT_CODES.keySet())
        {
            if(key.length() > max)
            {
                max = key.length();
            }
        }

        return max;
    }

    @ModifyVariable(method = "func_238340_a_", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static String iterateFormatted(String str, String p_238340_0_, int start, Style style, Style defaultStyle, ICharacterConsumer p_238340_4_)
    {
        if(!ClientConfig.isExtendedSyntaxEnabled.get() || !ClientConfig.isEnabled.get())
            return str;

        StringBuilder formatReplacedBuilder = new StringBuilder(str);

        for(int characterIndex = start; characterIndex < formatReplacedBuilder.length() /*Important not to use a variable because the length will change*/; characterIndex++)
        {
            int length = formatReplacedBuilder.length();
            char c = formatReplacedBuilder.charAt(characterIndex);

            if (c == '\u00A7') //§
            {
                if (characterIndex + 1 < length)
                {
                    //needs to be passed by reference, hence the MutableInt.
                    //silly java.
                    MutableInt muableCharacterIndex = new MutableInt(characterIndex + 1);

                    while(replaceFormatCodeInString(formatReplacedBuilder, muableCharacterIndex))
                    {
                        characterIndex = muableCharacterIndex.intValue() - 1;
                    }
                }
            }
        }

        return formatReplacedBuilder.toString();
    }

    private static boolean replaceFormatCodeInString(StringBuilder string, MutableInt startIndex /*this needs to be passed by reference, hence mutableint*/)
    {
        int length = string.length();

        if(startIndex.intValue() >= length)
            return false; //we did not do anything

        int closeBraceIndex = -1;
        char nextChar = string.charAt(startIndex.intValue());
        StringBuilder builder = new StringBuilder();

        if (nextChar == '[')
        {
            int maxLength = Math.min(startIndex.intValue() + LONGEST_FORMAT_CODE_LENGTH, length);

            for (int formatCharIndex = startIndex.intValue() + 1; formatCharIndex < maxLength; formatCharIndex++)
            {
                nextChar = string.charAt(formatCharIndex);

                if (nextChar == ']')
                {
                    closeBraceIndex = formatCharIndex;
                    break;
                }
                else
                {
                    builder.append(Character.toLowerCase(nextChar));
                }
            }
        }

        String formatString = builder.toString();

        if (!formatString.isEmpty() && closeBraceIndex != -1)
        {
            if (FORMAT_CODES.containsKey(formatString))
            {
                if(string.charAt(startIndex.intValue() - 1) != '\u00A7')
                {
                    string.insert(startIndex.intValue() ,'\u00A7');
                    startIndex.add(1);

                    closeBraceIndex++;
                }

                string.replace(startIndex.intValue(), closeBraceIndex + 1, FORMAT_CODES.get(formatString).toString());

                startIndex.add(1);
                return true; //signal that we successfully replaced something
            }
        }

        return false; //we did not replace anything
    }
}
