# Introduction #

Anyone, developer or not, can help to improve Quick Dice Roller by translating text or giving feed-back on current translations.

# Crowdin #

The easiest way to help with the translation is to go to the [Quick Dice Roller project hosted on Crowdin](https://crowdin.com/project/quick-dice-roller).

Here you can check the status of the translation on each language and give your contribute. It won't take more than few minutes, and the tool is very easy to use, so don't be shy :)

# Details #

As for all android apps, localized text is organized into XML resources with the following format:
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="string_id_01">Text 01</string>
    <string name="string_id_02">Text 02</string>
<resources>
```
Text that need translation is the part included in the _string_ tag (`Text 01` and `Text 02` in the example).

Different languages are stored in different files all with the same name (`string.xml`) but located in different folders.

Current translations:
  * [English](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Quick%20Dice/res/values/strings.xml) - you can use this as reference for other translations
  * [Italian](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Quick%20Dice/res/values-it/strings.xml) - you can use this as reference for other translations
  * [French](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Quick%20Dice/res/values-fr/strings.xml)
  * [Spanish](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Quick%20Dice/res/values-es/strings.xml) (partial)
  * [Deutch](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Quick%20Dice/res/values-de/strings.xml) (partial)

## Store description ##
Following files contains descriptions and details used in app stores.

These are simple text files for reference and don't need to follow any specific format.
  * [English](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Deploy%20QuickDice/desc-en.txt) - you can use this as reference for other translations
  * [Italian](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Deploy%20QuickDice/desc-it.txt) - you can use this as reference for other translations
  * [French](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Deploy%20QuickDice/desc-fr.txt)
  * [Spanish](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Deploy%20QuickDice/desc-es.txt) (partial)
  * [Deutch](https://code.google.com/p/quick-dice-roller/source/browse/trunk/Deploy%20QuickDice/desc-de.txt) (TODO)