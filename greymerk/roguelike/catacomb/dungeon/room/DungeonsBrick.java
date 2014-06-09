package greymerk.roguelike.catacomb.dungeon.room;

import greymerk.roguelike.catacomb.Catacomb;
import greymerk.roguelike.catacomb.dungeon.IDungeon;
import greymerk.roguelike.catacomb.theme.ITheme;
import greymerk.roguelike.treasure.TreasureChest;
import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.IBlockFactory;
import greymerk.roguelike.worldgen.MetaBlock;
import greymerk.roguelike.worldgen.Spawner;
import greymerk.roguelike.worldgen.WorldGenPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class DungeonsBrick implements IDungeon {

		
	public DungeonsBrick(){
	}
	
	public boolean generate(World world, Random rand, ITheme theme, int x, int y, int z) {
		
		MetaBlock stair = theme.getPrimaryStair();
		IBlockFactory blocks = theme.getPrimaryWall();
		IBlockFactory pillar = theme.getPrimaryPillar();
		
		// fill air inside
		WorldGenPrimitive.fillRectSolid(world, rand, x - 3, y, z - 3, x + 3, y + 3, z + 3, 0);
		WorldGenPrimitive.fillRectSolid(world, rand, x - 1, y + 4, z - 1, x + 1, y + 4, z + 1, 0);
		
		// shell
		WorldGenPrimitive.fillRectHollow(world, rand, x - 4, y - 1, z - 4, x + 4, y + 4, z + 4, blocks, false, true);

		Coord start;
		Coord end;
		Coord cursor;

		
		cursor = new Coord(x, y, z);
		cursor.add(Cardinal.UP, 5);
		WorldGenPrimitive.setBlock(world, cursor, 0);
		cursor.add(Cardinal.UP, 1);
		WorldGenPrimitive.setBlock(world, rand, cursor, blocks, true, true);
		
		// Chests
		List<Coord> space = new ArrayList<Coord>();
		
		for(Cardinal dir : Cardinal.directions){
			
			// top
			cursor = new Coord(x, y, z);
			cursor.add(dir, 1);
			cursor.add(Cardinal.UP, 5);
			stair.setMeta(WorldGenPrimitive.blockOrientation(Cardinal.reverse(dir), true));
			WorldGenPrimitive.setBlock(world, rand, cursor, stair, false, true);
			cursor.add(Cardinal.getOrthogonal(dir)[0], 1);
			WorldGenPrimitive.setBlock(world, rand, cursor, blocks, false, true);

			cursor = new Coord(x, y, z);
			cursor.add(dir, 2);
			cursor.add(Cardinal.UP, 4);
			WorldGenPrimitive.setBlock(world, cursor, 0);
			cursor.add(Cardinal.UP, 1);
			WorldGenPrimitive.setBlock(world, rand, cursor, blocks, false, true);
			
			// pillar
			cursor = new Coord(x, y, z);
			cursor.add(dir, 3);
			cursor.add(Cardinal.getOrthogonal(dir)[0], 3);
			start = new Coord(cursor);
			cursor.add(Cardinal.UP, 2);
			end = new Coord(cursor);
			WorldGenPrimitive.fillRectSolid(world, rand, start, end, pillar, true, true);
			cursor.add(Cardinal.UP, 1);
			WorldGenPrimitive.setBlock(world, rand, cursor, blocks, true, true);
			
			// pillar stairs
			for(Cardinal orth : Cardinal.getOrthogonal(dir)){
				cursor = new Coord(x, y, z);
				cursor.add(dir, 3);
				cursor.add(orth, 2);
				cursor.add(Cardinal.UP, 3);
				stair.setMeta(WorldGenPrimitive.blockOrientation(Cardinal.reverse(orth), true));
				WorldGenPrimitive.setBlock(world, rand, cursor, stair, true, true);
			}

			// layer above pillars
			cursor = new Coord(x, y, z);
			cursor.add(dir, 2);
			cursor.add(Cardinal.getOrthogonal(dir)[0], 2);
			cursor.add(Cardinal.UP, 4);
			WorldGenPrimitive.setBlock(world, rand, cursor, blocks, false, true);
			
			for(Cardinal orth : Cardinal.getOrthogonal(dir)){
				cursor = new Coord(x, y, z);
				cursor.add(Cardinal.UP, 4);
				cursor.add(dir, 2);
				cursor.add(orth, 1);
				stair.setMeta(WorldGenPrimitive.blockOrientation(Cardinal.reverse(orth), true));
				WorldGenPrimitive.setBlock(world, rand, cursor, stair, false, true);
			}
			
			cursor = new Coord(x, y, z);
			cursor.add(dir, 1);
			cursor.add(Cardinal.getOrthogonal(dir)[0], 1);
			cursor.add(Cardinal.UP, 5);
			WorldGenPrimitive.setBlock(world, rand, cursor, blocks, false, true);
			
			for(Cardinal orth : Cardinal.getOrthogonal(dir)){
				cursor = new Coord(x, y, z);
				cursor.add(dir, 3);
				cursor.add(orth, 2);
				space.add(cursor);
			}
		}

		List<TreasureChest> types = new ArrayList<TreasureChest>(Arrays.asList(TreasureChest.ARMOUR, TreasureChest.WEAPONS, TreasureChest.TOOLS));
		TreasureChest.createChests(world, rand, 1, space, types);
		
		Spawner.generate(world, rand, x, y, z);

		return true;
	}
	
	public boolean isValidDungeonLocation(World world, int x, int y, int z) {
		return false;
	}
	
	public int getSize(){
		return 4;
	}
}
