CAUTION: REQUIRES KOTLIN TO WORK! (You can use ASJCore for example)

If you have any questions or need help - https://discord.com/invite/SgBZVDwZQr

 

This library provides the opportunity to create multiblocks without hardcode in the mod: you can build a multiblock in the game, write the values you need for each position into a block and later use it in the code!
How it works? Build your structure, customise everything with special item, get json file, inherit MultiStructure class in code and customise it

more detailed:
The main item - Zone Selector. It has 2 modes, which are swapped by special keyButton(check settings):
a) Zone Selector. Used to create global variables for structure, specify the output path for json file and choose the world zone to write to file.
Left/ Right click - choose the bounds of the zone to write to file. For customisation click on air
What are global variables? It is as it is heard - you can create some variables for structure, specifing Name (left column, String value), Type (midddle menu button, click to swap), Value (Right column)
Variables are saved after closing the gui!

b) Block Customiser. Used to customise global variables for each block
Right click - open gui to customise the block you clicked on. Here you can change the values of global variables for current block (this block will get the specified values)
Shift Right Click - set this block as Master block (not really interesting for you, but required to calculate the offsets. To easily debug your structure, I recommend to set the center block as master)
Another Special button (check settings) - write structure to file
1) you are building your multiblock (those blocks from which it is created, for example, an infernal furnace made of obsidian, lava, etc.)
2) Using the zone Selector, you can set a set of "global variables" for your multiblock, set default values and then set the necessary private values for each block as you wish (standard variable types are supported, variable organization - key--->value)
3) you select the necessary zone, set the position of the master block, relative to which the displacement of the remaining blocks of the structure will be considered
4) specify the file name in the config where to write the result
5) you receive a json file containing information about each block of the structure: its offset relative to the master, the type of block, the meta and all the variables you manually set
6) you create, for example, a singleton inheriting a multistructure and customize it (specify the path to the previously received file, the ability to rotate, spinning the multiblock, additional conditions for creation / destruction, etc.)
7) create a class inheriting the MultiStructureElement, which symbolizes each block of your multiblock. You set which block should replace each block when creating a multiblock, and set additional conditions for creation.

9) You freely use previously recorded global variables and make the necessary checks (for example, you have written an int key in each block in the world, according to which you will then get the necessary tile entity from the array in the code in place of this block, which will take its place))

10) Write your TileEntity and blocks

 

NOTE: your structure class should consist of at least 3 classes:

1) extends MultiStructure - your main class to setup structure)

2)MultiStructureElement

represents each block of structure, is instantialised from JSON file, VERY IMPORTANT - ONLY VARIABLES SPECIFIED IN JSON ALLOWED HERE, IF NOT, THEY WILL BE SET TO NULL!!!! (Json problems)
MultiStructure contains this class objects, which are made from your json file. (By default it has x,y,z, storedBlock and meta). Here you should make a constructor and add (to default x,y,z etc) your own written global variables which you have made in game. Now you can use them in any purposes - for example, you can create "replacement block meta" variable and during the construction set this meta in order to set proper tileEntity later! (this gives ability not to hardcode offsets and specify necessary offsets in game)
3) AdditionalData - if you want to give MultiStructureElement any fields not from json (for example, texture location or, what is already done, block object), you should specify them in this class.

You can find corrent usage of this classes here: https://github.com/Suslovila/MultiBlockAPI_mod/blob/master/src/main/java/com/suslovila/sus_multi_blocked/common/multistructure/MultiBlockTower.kt

comments to settings:

availableFacings - by default you should build your structure as if it is "looking up". Then, in your class, specify the possible facings - if you want, your structure, but facing the East, will also be valid!
rotatable - imagine that you have already orientated your structure to some of six possible variants (UP, DOWN, etc), and now you want your strcucture to validate if spinning around this direction. Rotatable allows to do this!
sourcePath - path to your json with structure, example: "/assets/${Kharium.MOD_ID}/structures/kharu_snare.json"
dataClass - your MultiStructureElement java class object (needed for instantiation)
validationType - determines if structure should be validated by clicking on any block or only the master block (optimisation purposes: if you have a huge structure (100 blocks and more), it is recommended to use VALIDATION_TYPE.MASTER)
 
 
An example structure is included in mod (disabled) - Tower


 

If you get lost, here is a toy example:

 

 

here is more complicated result of using library (thaumcraft 4.2.3.5 addon : https://github.com/Suslovila/Kharium/blob/76dae9112607999bd968ea9cd7bf0e142dc966e8/src/main/java/com/suslovila/kharium/common/multiStructure/kharuSnare/MultiStructureKharuSnare.kt)

