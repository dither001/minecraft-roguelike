package greymerk.roguelike.worldgen;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import greymerk.roguelike.treasure.ITreasureChest;
import greymerk.roguelike.treasure.TreasureManager;
import greymerk.roguelike.worldgen.blocks.BlockType;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class WorldEditor implements IWorldEditor {

  private static List<Material> invalid;
  World world;
  private Map<Block, Integer> stats;
  private TreasureManager chests;

  static {
    invalid = new ArrayList<>();
    invalid.add(Material.WOOD);
    invalid.add(Material.WATER);
    invalid.add(Material.CACTUS);
    invalid.add(Material.SNOW);
    invalid.add(Material.GRASS);
    invalid.add(Material.GOURD);
    invalid.add(Material.LEAVES);
    invalid.add(Material.PLANTS);
  }

  public WorldEditor(World world) {
    this.world = world;
    stats = new HashMap<>();
    chests = new TreasureManager();
  }

  private boolean setBlock(Coord pos, MetaBlock block, int flags, boolean fillAir, boolean replaceSolid) {

    MetaBlock currentBlock = getBlock(pos);

    if (currentBlock.getBlock() == Blocks.CHEST) {
      return false;
    }
    if (currentBlock.getBlock() == Blocks.TRAPPED_CHEST) {
      return false;
    }
    if (currentBlock.getBlock() == Blocks.MOB_SPAWNER) {
      return false;
    }

    //boolean isAir = world.isAirBlock(pos.getBlockPos());
    boolean isAir = currentBlock.getBlock() == Blocks.AIR;

    if (!fillAir && isAir) {
      return false;
    }
    if (!replaceSolid && !isAir) {
      return false;
    }

    try {
      world.setBlockState(pos.getBlockPos(), block.getState(), flags);
    } catch (NullPointerException npe) {
      //ignore it.
    }

    Block type = block.getBlock();
    stats.merge(type, 1, Integer::sum);
    return true;

  }

  @Override
  public boolean setBlock(Coord pos, MetaBlock block, boolean fillAir, boolean replaceSolid) {
    return setBlock(pos, block, block.getFlag(), fillAir, replaceSolid);
  }

  @Override
  public boolean isAirBlock(Coord pos) {
    return world.isAirBlock(pos.getBlockPos());
  }

  @Override
  public long getSeed() {
    return world.getSeed();
  }

  @Override
  public Random getSeededRandom(int a, int b, int c) {
    return world.setRandomSeed(a, b, c);
  }

  @Override
  public void spiralStairStep(Random rand, Coord origin, IStair stair, IBlockFactory fill) {

    MetaBlock air = BlockType.get(BlockType.AIR);
    Coord cursor;
    Coord start;
    Coord end;

    start = new Coord(origin);
    start.translate(new Coord(-1, 0, -1));
    end = new Coord(origin);
    end.translate(new Coord(1, 0, 1));

    RectSolid.fill(this, rand, start, end, air);
    fill.set(this, rand, origin);

    Cardinal dir = Cardinal.directions[origin.getY() % 4];
    cursor = new Coord(origin);
    cursor.translate(dir);
    stair.setOrientation(dir.antiClockwise(), false).set(this, cursor);
    cursor.translate(dir.clockwise());
    stair.setOrientation(dir.clockwise(), true).set(this, cursor);
    cursor.translate(dir.reverse());
    stair.setOrientation(dir.reverse(), true).set(this, cursor);
  }

  @Override
  public void fillDown(Random rand, Coord origin, IBlockFactory blocks) {

    Coord cursor = new Coord(origin);

    while (!getBlock(cursor).isOpaqueCube() && cursor.getY() > 1) {
      blocks.set(this, rand, cursor);
      cursor.translate(Cardinal.DOWN);
    }
  }

  @Override
  public MetaBlock getBlock(Coord pos) {
    return new MetaBlock(world.getBlockState(pos.getBlockPos()));
  }

  @Override
  public TileEntity getTileEntity(Coord pos) {
    return world.getTileEntity(pos.getBlockPos());
  }

  @Override
  public boolean validGroundBlock(Coord pos) {
    if (isAirBlock(pos)) {
      return false;
    }
    return !invalid.contains(getBlock(pos).getMaterial());
  }

  @Override
  public int getStat(Block type) {
    if (!stats.containsKey(type)) {
      return 0;
    }
    return stats.get(type);
  }

  @Override
  public Map<Block, Integer> getStats() {
    return stats;
  }

  @Override
  public void addChest(ITreasureChest toAdd) {
    chests.add(toAdd);
  }

  @Override
  public TreasureManager getTreasure() {
    return chests;
  }

  @Override
  public boolean canPlace(MetaBlock block, Coord pos, Cardinal dir) {
    if (!isAirBlock(pos)) {
      return false;
    }
    return block.getBlock().canPlaceBlockOnSide(world, pos.getBlockPos(), dir.getFacing());
  }

  @Override
  public IPositionInfo getInfo(Coord pos) {
    return new PositionInfo(world, pos);
  }

  @Override
  public Coord findNearestStructure(VanillaStructure type, Coord pos) {

    ChunkProviderServer chunkProvider = ((WorldServer) world).getChunkProvider();
    String structureName = VanillaStructure.getName(type);

    BlockPos structurebp = null;

    try {
      structurebp = chunkProvider.getNearestStructurePos(world, structureName, pos.getBlockPos(), false);
    } catch (NullPointerException e) {
      // happens for some reason if structure type is disabled in Chunk Generator Settings
    }

    if (structurebp == null) {
      return null;
    }

    return new Coord(structurebp);
  }

  @Override
  public String toString() {
    return stats.entrySet().stream()
        .map(pair -> pair.getKey().getLocalizedName() + ": " + pair.getValue() + "\n")
        .collect(Collectors.joining());
  }
}

