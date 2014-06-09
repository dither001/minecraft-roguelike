package greymerk.roguelike.catacomb.dungeon.room;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import greymerk.roguelike.catacomb.dungeon.IDungeon;
import greymerk.roguelike.catacomb.theme.ITheme;
import greymerk.roguelike.config.RogueConfig;
import greymerk.roguelike.treasure.ITreasureChest;
import greymerk.roguelike.treasure.TreasureChest;
import greymerk.roguelike.treasure.TreasureChestEmpty;
import greymerk.roguelike.treasure.loot.Potion;
import greymerk.roguelike.treasure.loot.Loot;
import greymerk.roguelike.treasure.loot.PotionMixture;
import greymerk.roguelike.treasure.loot.Record;
import greymerk.roguelike.treasure.loot.provider.ItemArmour;
import greymerk.roguelike.treasure.loot.provider.ItemNovelty;
import greymerk.roguelike.worldgen.BlockFactoryCheckers;
import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Log;
import greymerk.roguelike.worldgen.MetaBlock;
import greymerk.roguelike.worldgen.WorldGenPrimitive;

public class DungeonBTeam implements IDungeon {

	@Override
	public boolean generate(World world, Random rand, ITheme theme, int x, int y, int z) {
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y, z - 4, x + 4, y + 4, z + 5, 0);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y - 1, z - 4, x + 4, y - 1, z + 5, Block.cobblestone.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 3, y - 1, z - 2, x + 3, y - 1, z + 3, new MetaBlock(Block.stainedClay.blockID, 9), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 2, y - 1, z - 1, x + 2, y - 1, z + 2, new MetaBlock(43, 8), true, true);
		
		// wood panel walls
		
		BlockFactoryCheckers panels = new BlockFactoryCheckers(Log.getLog(Log.SPRUCE, Cardinal.UP), Log.getLog(Log.SPRUCE, Cardinal.EAST));
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y + 1, z + 6, x + 4, y + 3, z + 6, panels, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y + 1, z - 5, x + 4, y + 3, z - 5, panels, true, true);
	
		MetaBlock spruce = new MetaBlock(Block.planks.blockID, 1); 
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y, z + 6, x + 4, y, z + 6, spruce, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y, z - 5, x + 4, y, z - 5, spruce, true, true);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 5, y - 1, z - 4, x - 5, y, z + 5, spruce, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y - 1, z - 4, x + 5, y, z + 5, spruce, true, true);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y + 4, z - 4, x + 4, y + 4, z - 4, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.SOUTH, true)), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y + 4, z + 5, x + 4, y + 4, z + 5, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.NORTH, true)), true, true);
		
		// doors
		WorldGenPrimitive.fillRectSolid(world, rand, x - 1, y, z - 5, x + 1, y + 2, z - 5, Block.cobblestone.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x, y, z - 5, x, y + 1, z - 5, 0);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 1, y, z + 6, x + 1, y + 2, z + 6, Block.cobblestone.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x, y, z + 6, x, y + 1, z + 6, 0);
		
		// west wall
		WorldGenPrimitive.fillRectSolid(world, rand, x - 5, y, z - 5, x - 5, y + 4, z + 6, spruce, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 5, y, z - 1, x - 5, y, z + 2, Block.music.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 5, y + 1, z - 3, x - 5, y + 3, z + 4, Block.bookShelf.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 5, y + 1, z - 1, x - 5, y + 3, z + 2, new MetaBlock(Block.cloth.blockID, 15), true, true);
		
		MetaBlock cocao = new MetaBlock(Block.cocoaPlant.blockID, 9);
		WorldGenPrimitive.setBlock(world, x - 5, y + 2, z - 2, Log.getLog(Log.JUNGLE, Cardinal.EAST));
		cocao.setBlock(world, x - 4, y + 2, z - 2);
		WorldGenPrimitive.setBlock(world, x - 5, y + 2, z + 3, Log.getLog(Log.JUNGLE, Cardinal.EAST));
		cocao.setBlock(world, x - 4, y + 2, z + 3);
		
		lamp(world, x - 2, y, z - 4);
		lamp(world, x + 2, y, z - 4);
		lamp(world, x - 2, y, z + 5);
		lamp(world, x + 2, y, z + 5);
		
		// east wall
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y + 1, z - 4, x + 5, y + 4, z + 5, Block.stoneBrick.blockID);
		
		MetaBlock greenBlock = RogueConfig.getBoolean(RogueConfig.PRECIOUSBLOCKS) ? new MetaBlock(Block.blockEmerald.blockID) : new MetaBlock(Block.cloth.blockID, 5);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y, z - 1, x + 5, y + 4, z - 1, greenBlock, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y, z, x + 5, y, z + 1, greenBlock, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y + 1, z, x + 5, y + 1, z + 1, 0);
		greenBlock.setBlock(world, x + 5, y + 1, z + 2);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y + 2, z, x + 5, y + 2, z + 1, greenBlock, true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y + 3, z, x + 5, y + 3, z + 1, 0);
		greenBlock.setBlock(world, x + 5, y + 3, z + 2);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 5, y + 4, z, x + 5, y + 4, z + 1, greenBlock, true, true);
		
		// roof
		WorldGenPrimitive.fillRectSolid(world, rand, x - 4, y + 5, z - 5, x + 4, y + 6, z + 6, Block.stone.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 2, y + 5, z - 1, x + 2, y + 5, z - 1, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.SOUTH, true)), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 2, y + 5, z + 2, x + 2, y + 5, z + 2, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.NORTH, true)), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 3, y + 5, z, x - 3, y + 5, z + 1, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.EAST, true)), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 3, y + 5, z, x + 3, y + 5, z + 1, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.WEST, true)), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 2, y + 5, z, x + 2, y + 5, z + 1, Block.redstoneLampIdle.blockID);
		
		WorldGenPrimitive.setBlock(world, x - 3, y + 5, z - 1, Block.wood.blockID);
		WorldGenPrimitive.setBlock(world, x + 3, y + 5, z - 1, Block.wood.blockID);
		
		WorldGenPrimitive.setBlock(world, x - 3, y + 5, z + 2, Block.wood.blockID);
		WorldGenPrimitive.setBlock(world, x + 3, y + 5, z + 2, Block.wood.blockID);
		
		
		// table
		WorldGenPrimitive.fillRectSolid(world, rand, x - 2, y, z, x - 2, y, z + 1, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.WEST, true)), true, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 2, y, z, x + 2, y, z + 1, new MetaBlock(Block.stairsWoodSpruce.blockID, WorldGenPrimitive.blockOrientation(Cardinal.EAST, true)), true, true);
		MetaBlock spruceSlabUpsideDown = new MetaBlock(126, 9);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 1, y, z, x + 1, y, z + 1, spruceSlabUpsideDown, true, true);
		
		// chairs
		MetaBlock chair = new MetaBlock(Block.stairsNetherBrick.blockID);
		chair.setMeta(WorldGenPrimitive.blockOrientation(Cardinal.SOUTH, false));
		chair.setBlock(world, x - 1, y, z - 2);
		chair.setBlock(world, x + 1, y, z - 2);
		
		chair.setMeta(WorldGenPrimitive.blockOrientation(Cardinal.NORTH, false));
		chair.setBlock(world, x - 1, y, z + 3);
		chair.setBlock(world, x + 1, y, z + 3);
		
		// wall entrances
		WorldGenPrimitive.fillRectSolid(world, rand, x - 7, y - 1, z - 4, x - 6, y + 4, z + 4, Block.stoneBrick.blockID);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 6, y - 1, z - 4, x + 7, y + 4, z + 4, Block.stoneBrick.blockID);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 7, y, z - 3, x + 7, y + 1, z - 3, 0);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 7, y, z + 4, x + 7, y + 1, z + 4, 0);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 7, y, z - 3, x - 7, y + 1, z + 4, 0);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 7, y, z - 3, x + 7, y + 1, z + 4, 0);
		
		WorldGenPrimitive.fillRectSolid(world, rand, x - 8, y - 1, z - 4, x - 8, y + 4, z + 4, new MetaBlock(Block.stoneBrick.blockID), false, true);
		WorldGenPrimitive.fillRectSolid(world, rand, x + 8, y - 1, z - 4, x + 8, y + 4, z + 4, new MetaBlock(Block.stoneBrick.blockID), false, true);
		
		
		ITreasureChest recordChest = new TreasureChestEmpty().generate(world, rand, x - 4, y, z - 4);
		recordChest.setInventorySlot(Record.getRecord(Record.STAL), recordChest.getInventorySize() / 2);
		WorldGenPrimitive.setBlock(world, x - 3, y, z - 4, Block.jukebox.blockID);
		
		ITreasureChest bdubsChest = new TreasureChestEmpty().generate(world, rand, x - 3, y, z + 5);
		bdubsChest.setInventorySlot(ItemNovelty.getItem(ItemNovelty.BDOUBLEO), (bdubsChest.getInventorySize() / 2) - 2);
		
		ItemStack shirt = new ItemStack(Item.plateLeather);
		Loot.setItemName(shirt, "Pink Sweater", null);
		Loot.setItemLore(shirt, "\"It's chinese red!\"");
		ItemArmour.dyeArmor(shirt, 250, 96, 128);
		
		bdubsChest.setInventorySlot(shirt, (bdubsChest.getInventorySize() / 2) + 2);
		
		ITreasureChest gennybChest = new TreasureChestEmpty().generate(world, rand, x + 3, y, z + 5);
		gennybChest.setInventorySlot(ItemNovelty.getItem(ItemNovelty.GENERIKB), gennybChest.getInventorySize() / 2);
		
		
		WorldGenPrimitive.setBlock(world, x + 4, y, z - 4, Block.bookShelf.blockID);
		WorldGenPrimitive.setBlock(world, x + 4, y + 1, z - 4, Block.brewingStand.blockID);
		
		ITreasureChest contraband = new TreasureChestEmpty().generate(world, rand, x + 3, y, z - 4);
		
		for(int i = 0; i < 8; ++i){
			ItemStack booze = PotionMixture.getBooze(rand);
			contraband.setInventorySlot(booze, rand.nextInt(contraband.getInventorySize()));
		}
		
		return true;
	}

	private static void lamp(World world, int x, int y, int z){
		MetaBlock spruce = new MetaBlock(Block.planks.blockID, 1);
		spruce.setBlock(world, x, y + 4, z);
		WorldGenPrimitive.setBlock(world, x, y + 3, z, Block.fence.blockID);
		
		WorldGenPrimitive.setBlock(world, x, y + 2, z, Block.glowStone.blockID);
		MetaBlock fence = new MetaBlock(Block.trapdoor.blockID, 4);
		fence.setBlock(world, x, y + 2, z - 1);
		fence.setMeta(5);
		fence.setBlock(world, x, y + 2, z + 1);
		fence.setMeta(6);
		fence.setBlock(world, x - 1, y + 2, z);		
		fence.setMeta(7);
		fence.setBlock(world, x + 1, y + 2, z);
		
		WorldGenPrimitive.setBlock(world, x, y + 1, z, Block.fence.blockID);
		spruce.setBlock(world, x, y, z);
	}
	
	public int getSize(){
		return 8;
	}
	
}
