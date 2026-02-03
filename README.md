# FarmersCreate

![Logo of the mod](media/FarmersCreateLogo.png)

This is a Fabric mod for Minecraft version 1.21.11 that introduces compatibility between Farmer's Delight and Create. 

It automatically adds a heated mixing recipe for every Farmer's Delight cooking pot recipe and a deployer recipe 
(the deployer needs to have a knife mounted) for every cutting board recipe. 

## Supported Farmer's Delight addons
Since the mod dynamically creates recipes on the fly, it should work with any addon. I will note known 
incompatibilities here if any are found.

## How it works
This mod uses Mixins to 
- hook into the recipe registration process to add the recipes.
- hook into the deployer activation to add rolled results for cutting board recipes.

The fork by ZurrTum does not allow multiple results for a deployer recipe, which is why the second mixin is necessary. 
Also, it does not support CustomingredientImpl, which forces me to hardcode recipes that use it. 
Two Farmer's Delight recipes do that and are therefore supported. Other recipes that use CustomingredientImpl are 
not supported and skipped. 

## Known issues/limitations
Since the unofficial Create mod fork for 1.21.11 does not allow multiple results for a deployer recipe, these results 
are not shown in JEI. However, they still work because of the workaround I am deploying.

## Credits
This mod would not have come to life without the 
[Create mod Fabric port by ZurrTum](https://github.com/ZurrTum/Create-Fly) and the 
[Farmer's Delight Refabricated mod](https://github.com/MehVahdJukaar/FarmersDelightRefabricated). 
Furthermore, inspiration is drawn from the 
[Create: Slice and Dice mod](https://github.com/PssbleTrngle/SliceAndDice/), which sadly was not updated to 1.21.11. 
And last but not least, the original [Creators of Create](https://github.com/Creators-of-Create/Create) and 
[Farmer's Delight](https://github.com/vectorwing/FarmersDelight).