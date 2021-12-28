# Chat Colours
Allows the use of colours/formatting in chat, item names, and everywhere that allows you to type.

# Extended format syntax

The mod allows you to use named colours instead of the normal single letters.

The names are __white, black, blue, green, aqua (or cyan), red, purple, gray and yellow__. 

The names are not case sensitive.

To use the dark version of the colour (except white and black), put "d", "dark" or "deep" before or after the name. 
You can optionaly use a space or underscore between the name and the prefix/suffix.
For example, ``§[dark_blue]``, ``§[deepblue]``, ``§[blue d]``, ``§[dblue]`` all make dark blue text

`§[gold]`, the name MCWiki gives the colour, can also be used for dark yellow.

## Bold, underline, italics, etc
You can also use:
 - `§[s]` for strikeout text, 
 - `§[b]` for bold text,
 - `§[o]` for obfuscated text, 
 - `§[u]` for underlined text, 
 - and `§[r]` for §r (reset)

## Multiple style options
Format options can be chained without needing multiple `§`s. For example, `§[red][u][i]text` will show red, bold, underlined text. You need to put the colour before the bold and underline. This is because the colour code resets the style to be coloured, but not italic, bold, etc. This is a minecraft thing, and also applies to vanilla formatting codes.

# Use on servers
If you want to use this mod on a server, this mod will have to be installed on it, or the server must otherwise allow you to send '§' in chat, or the server will automatically kick you when you use formatting.
