package timeTraveler.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureVillagePieces;
/**
 * Contains information about the future mechanics
 * @author Charsmud
 *
 */
public class FutureTravelMechanics
{
	
	/**
	 * Constructor
	 */
	public FutureTravelMechanics()
	{
	}
	Random random = new Random();
	/**
	 * Main expanding forests method
	 * @param world
	 * @param size
	 */
	public void expandForests(World world, int size)
	{
		if(ClientMethods.isSinglePlayer())
		{
	         Iterator<ChunkCoordIntPair> iterator = world.activeChunkSet.iterator();
	         ArrayList<ChunkCoordinates> list = new ArrayList<ChunkCoordinates>();
	         ArrayList<Integer> treeTypes = new ArrayList<Integer>();
	         while(iterator.hasNext())
	         {
	                 ChunkCoordIntPair coords = iterator.next();
	                 Chunk currentScanningChunk = world.getChunkFromChunkCoords(coords.chunkXPos, coords.chunkZPos);
	                 int h = currentScanningChunk.getTopFilledSegment();
	                 for (int i = 0; i < 8; ++i) {
	                         int x = random.nextInt(16);
	                         int z = random.nextInt(16);
	                         int y = currentScanningChunk.getHeightValue(x, z) - 1;
	                         if (y >= 0 && currentScanningChunk.getBlockID(x, y, z) == Block.leaves.blockID) {
	                         double dx = random.nextDouble();
	                         double dz = random.nextDouble();
	                         double _ = (double)(dx * dx + dz * dz);
	                         _ = 8.0D / MathHelper.sqrt_double(_) + 8.0 * random.nextDouble() * random.nextDouble();
	                         int x_ = (int)(dx * _) + (x += currentScanningChunk.xPosition << 4);
	                         int z_ = (int)(dz * _) + (z += currentScanningChunk.zPosition << 4);
	                         int y_ = world.getHeightValue(x_, z_) - 1;
	                         if (y_ == -1 || world.getBlockId(x_, y_, z_) != Block.grass.blockID || world.getBlockId(x_, y_ + 1, z_) != 0 || (y - y_) > 32 || (y - y_) < 3)
	                         continue;
	                         list.add(new ChunkCoordinates(x_, y_ + 1, z_));
	                         treeTypes.add(world.getBlockMetadata(x, y, z));
	                         }
	                 }
	         }
	         System.out.println(">> " + list.size());
	         Iterator<Integer> typeIterator = treeTypes.iterator();
	         for (ChunkCoordinates coord : list)
	         {
	        	 if (typeIterator.hasNext())
	             {
	        		 growTree(typeIterator.next() & 3, world, coord.posX, coord.posY, coord.posZ, random);
	             }
	         }
		}
	}
	/**
	 * Grows the trees
	 * @param meta
	 * @param par1World
	 * @param par2
	 * @param par3
	 * @param par4
	 * @param par5Random
	 */
	public void growTree(int meta, World par1World, int par2, int par3, int par4, Random par5Random)
	{
	         int l = meta & 3;
	         Object object = null;
	         int i1 = 0;
	         int j1 = 0;
	         boolean flag = false;

	         if (l == 1)
	         {
	                 object = new WorldGenTaiga2(true);
	         }
	         else if (l == 2)
	         {
	                 object = new WorldGenForest(true);
	         }
	         else if (l == 3)
	         {
	         if (par5Random.nextInt(10) == 0)
	                 {
	         object = new WorldGenHugeTrees(true, 10 + par5Random.nextInt(20), 3, 3);
	                 }

	                 if (object == null)
	                 {
	                         j1 = 0;
	                         i1 = 0;
	                         object = new WorldGenTrees(true, 4 + par5Random.nextInt(7), 3, 3, false);
	                 }
	         }
	         else
	         {
	                 object = new WorldGenTrees(true);

	                 if (par5Random.nextInt(10) == 0)
	                 {
	                         object = new WorldGenBigTree(true);
	                 }
	         }
	         ((WorldGenerator)object).generate(par1World, par5Random, par2 + i1, par3, par4 + j1);
	}
	/**
	 * Ore Mappings and Max Ore Range
	 */
    public static final Map<Block, Tuple> oreExpansion = new HashMap();
    { // value range for maximum ore in a chunk
            oreExpansion.put(Block.oreRedstone, new Tuple(150, 200)); //24.8  25,50
            oreExpansion.put(Block.oreIron, new Tuple(150, 300)); //77  77,154
            oreExpansion.put(Block.oreCoal, new Tuple(140, 200)); // 142.6  143, 284
            oreExpansion.put(Block.oreGold, new Tuple(70, 140)); //8.2  8, 16
            oreExpansion.put(Block.oreLapis, new Tuple(50, 100)); //3.43  3, 7
            oreExpansion.put(Block.oreDiamond, new Tuple(50, 100)); //3.097  3, 6
    }

    /**
     * Contains Chunk Stuff
     */
    public static WeakHashMap<Chunk, Long> basis = new WeakHashMap();
    
    /**
     * Expands ores
     * @param world
     */
    public void expandOres(World world)
    {
    	if(ClientMethods.isSinglePlayer())
    	{
            Iterator<ChunkCoordIntPair> iterator = world.activeChunkSet.iterator();
            
            Map<ChunkCoordinates, Integer> placements = new HashMap();
            
            Set<Integer> oreIDs = new HashSet();
            for (Block block : oreExpansion.keySet())
                    oreIDs.add(block.blockID);
            
            while(iterator.hasNext())
            {
            	System.out.println("ITERATOR");
                    ChunkCoordIntPair coords = iterator.next();
                    Chunk currentScanningChunk = world.getChunkFromChunkCoords(coords.chunkXPos, coords.chunkZPos);
                    long base;
                    if (!basis.containsKey(currentScanningChunk))
                            basis.put(currentScanningChunk, base = currentScanningChunk.getRandomWithSeed(253L).nextLong());
                    else
                            base = basis.get(currentScanningChunk);
                    Map<Integer, List<ChunkCoordinates>> possible = new HashMap();
                    for (int i : oreIDs)
                            possible.put(i, new ArrayList());
                    ExtendedBlockStorage[] blockStorage = currentScanningChunk.getBlockStorageArray();
                    for (int i = 0; i < blockStorage.length && blockStorage[i] != null; ++i)
                    {
                    	System.out.println("BLOCKSLSB");
                            ExtendedBlockStorage storage = blockStorage[i];
                            byte[] blocksLSB = storage.getBlockLSBArray();
                            for (int j = 0; j < blocksLSB.length; ++j)
                            {
                                    if (blocksLSB[j] != 0 && oreIDs.contains(blocksLSB[j]&255))
                                    {
                                    	System.out.println("POSSIBLE");
                                            possible.get(blocksLSB[j]&255).add(new ChunkCoordinates((j & 15) + (coords.chunkXPos << 4), (j >> 8) + storage.getYLocation(), (j >> 4 & 15) + (coords.chunkZPos << 4)));
                                    }
                            }
                    }
                    for (Map.Entry<Block, Tuple> oreProbability : oreExpansion.entrySet())
                    {
                    	System.out.println("OREPROBABLILITY");
                            int blockID = oreProbability.getKey().blockID;
                            List<ChunkCoordinates> genCoords = possible.get(blockID);
                            if (genCoords.size() == 0)
                                    continue;
                            Tuple t = oreProbability.getValue();
                            int min = (Integer)t.getFirst();
                            int max = (Integer)t.getSecond();
                            int chunkCount = (int)((base ^ blockID * 6364136223846793005L + 1442695040888963407L) % (max - min + 1) + min);
                            int attempts = chunkCount - genCoords.size();
                            if (attempts > 0)
                            {
                            	System.out.println("ATTEMPTS");
                                    attempts = random.nextInt(attempts + 1);
                                    attempts = random.nextInt(attempts + 1);
                                    for (int i = 0; i < attempts && genCoords.size() > 0; i ++)
                                    {
                                            placements.put(genCoords.remove(random.nextInt(genCoords.size())), blockID);
                                    }
                            }
                    }
            }
            for (Iterator<Map.Entry<ChunkCoordinates, Integer>> iter = placements.entrySet().iterator(); iter.hasNext();)
            {
            	System.out.println("MAP.ENTRY");
                    Map.Entry<ChunkCoordinates, Integer> entry = iter.next();
                    ChunkCoordinates location = entry.getKey();
                    int x = location.posX, y = location.posY, z = location.posZ;
                    switch (random.nextInt(6))
                    {
                            case 0:
                                    --y;
                                    break;
                            case 1:
                                    ++y;
                                    break;
                            case 2:
                                    --z;
                                    break;
                            case 3:
                                    ++z;
                                    break;
                            case 4:
                                    --x;
                                    break;
                            case 5:
                                    ++x;
                    }
                if (world.getBlockId(x, y, z) == Block.stone.blockID)
                {
                    System.out.println("SETBLOCK");
                    world.setBlock(x, y, z, entry.getValue());
                }
            }
    	}
    }

    
    public void expandVillages(World world)
    {
    	VillageCollection worldVillages = world.villageCollectionObj;
    	List<VillageCollection> villageList = worldVillages.getVillageList();
    
        Iterator iterator = villageList.iterator();
        //StructureVillagePieces villagePieces = new StructureVillagePieces();
        while(iterator.hasNext())
        {
        	Village village = (Village)iterator.next();
        	//village.
        	//ArrayList listOfPieces = villagePieces.getStructureVillageWeightedPieceList(new Random(), 9);
               
        }
    }
    public static void launchWorld(String folderName, String worldName, WorldSettings ws)
    {
    	if(FMLClientHandler.instance().getClient().isSingleplayer())
    	{
    		FMLClientHandler.instance().getClient().launchIntegratedServer(folderName, worldName, ws);
    	}
    }
}
	

