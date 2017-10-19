package worldcontrolteam.worldcontrol.screen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class Screen {
    public int minX, minY, minZ;
    public int maxX, maxY, maxZ;
    private BlockPos corePos;
    private boolean isPowered = false;


    public TileEntityInfoPanel getCore(IBlockAccess world) {
        TileEntity tile = world.getTileEntity(corePos);
        return (TileEntityInfoPanel) tile;
    }

    public void setCore(TileEntityInfoPanel panel) {
        this.corePos = panel.getPos();
        this.isPowered = panel.getPowered();
    }

    public boolean isBlockNearby(TileEntity tileEntity) {
        int x = tileEntity.getPos().getX();
        int y = tileEntity.getPos().getY();
        int z = tileEntity.getPos().getZ();
        return (x == minX - 1 && y >= minY && y <= maxY && z >= minZ && z <= maxZ)
                || (x == maxX + 1 && y >= minY && y <= maxY && z >= minZ && z <= maxZ)
                || (x >= minX && x <= maxX && y == minY - 1 && z >= minZ && z <= maxZ)
                || (x >= minX && x <= maxX && y == maxY + 1 && z >= minZ && z <= maxZ)
                || (x >= minX && x <= maxX && y >= minY && y <= maxY && z == minZ - 1)
                || (x >= minX && x <= maxX && y >= minY && y <= maxY && z == maxZ + 1);
    }

    public boolean isBlockPartOf(TileEntity tileEntity) {
        int x = tileEntity.getPos().getX();
        int y = tileEntity.getPos().getY();
        int z = tileEntity.getPos().getZ();
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public void init(World world, boolean force) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity == null || !(tileEntity instanceof IScreenPart))
                        continue;
                    ((IScreenPart) tileEntity).setScreen(this);
                    if (isPowered || force) {
                        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                        world.checkLight(pos);
                    }
                }
            }
        }
    }

    public void destroy(boolean force, World world) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity == null || !(tileEntity instanceof IScreenPart))
                        continue;
                    IScreenPart part = (IScreenPart) tileEntity;
                    Screen targetScreen = part.getScreen();
                    if (targetScreen != null && targetScreen.equals(this)) {
                        part.setScreen(null);
                        part.updateData();
                    }
                    if (isPowered || force) {
                        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                        world.checkLight(pos);
                    }
                }
            }
        }
    }

    public void turnPower(World world, boolean on) {
        if (isPowered != on) {
            isPowered = on;
            markUpdate(world);
        }
    }

    public void markUpdate(World world) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    world.checkLight(pos);
                }
            }
        }
    }

    public int getDx() {
        return maxX - minX;
    }

    public int getDy() {
        return maxY - minY;
    }

    public int getDz() {
        return maxZ - minZ;
    }

    public NBTTagCompound toTag() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("minX", minX);
        tag.setInteger("minY", minY);
        tag.setInteger("minZ", minZ);

        tag.setInteger("maxX", maxX);
        tag.setInteger("maxY", maxY);
        tag.setInteger("maxZ", maxZ);

        tag = NBTUtils.writeBlockPos(tag, corePos);

        return tag;
    }

    public int getHeight(TileEntityInfoPanel core) {
        if (core == null)
            return 0;
        int rotation = core.getFacing().getIndex();
        switch (core.getFacing()) {
            case DOWN:
            case UP:
                if (rotation == 0 || rotation == 3)
                    return getDz() + 1;
                else
                    return getDx() + 1;
            case NORTH:
            case SOUTH:
                if (rotation == 0 || rotation == 3)
                    return getDy() + 1;
                else
                    return getDx() + 1;
            case EAST:
            case WEST:
                if (rotation == 0 || rotation == 3)
                    return getDy() + 1;
                else
                    return getDz() + 1;
        }
        return 1;
    }

    public int getWidth(TileEntityInfoPanel core) {
        if (core == null)
            return 0;
        int rotation = core.getFacing().getIndex();
        switch (core.getFacing()) {
            case DOWN:
            case UP:
                if (rotation == 0 || rotation == 3)
                    return getDx() + 1;
                else
                    return getDz() + 1;
            case NORTH:
            case SOUTH:
                if (rotation == 0 || rotation == 3)
                    return getDx() + 1;
                else
                    return getDy() + 1;
            case EAST:
            case WEST:
                if (rotation == 0 || rotation == 3)
                    return getDz() + 1;
                else
                    return getDy() + 1;
        }
        return 1;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + corePos.getX();
        result = prime * result + corePos.getY();
        result = prime * result + corePos.getZ();
        result = prime * result + maxX;
        result = prime * result + maxY;
        result = prime * result + maxZ;
        result = prime * result + minX;
        result = prime * result + minY;
        result = prime * result + minZ;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Screen other = (Screen) obj;
        return corePos == ((Screen) obj).corePos && maxX == other.maxX && maxY == other.maxY && maxZ == other.maxZ && minX == other.minX && minY == other.minY && minZ == other.minZ;
    }
}