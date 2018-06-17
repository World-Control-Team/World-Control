package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;

import javax.annotation.Nullable;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class TileEntityInfoPanelExtender extends TileEntity {
    public BlockPos origin;
    public EnumFacing facing = EnumFacing.NORTH;

    public TileEntityInfoPanelExtender() {

    }

    public void init() {
        this.facing = (EnumFacing) world.getBlockState(getPos()).getProperties().get(BlockInfoPanel.FACING);

    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        addPosInfo(compound);
        SPacketUpdateTileEntity spute = new SPacketUpdateTileEntity(
                getPos(), 2, compound
        );
        return spute;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if (pkt.getTileEntityType() != 2) return;
        NBTTagCompound compound = pkt.getNbtCompound();
        if (compound.getBoolean("null")) {
            this.origin = null;
        }
        else {
            this.origin = new BlockPos(
                    compound.getInteger("ox"),
                    compound.getInteger("oy"),
                    compound.getInteger("oz")
            );
        }
        IBlockState bs = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), bs, bs, 0);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        addPosInfo(compound);
        compound.setInteger("facing", facing.getIndex());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.getBoolean("null")) {
            this.origin = null;
        }
        else {
            this.origin = new BlockPos(
                    compound.getInteger("ox"),
                    compound.getInteger("oy"),
                    compound.getInteger("oz")
            );
        }

        this.facing = EnumFacing.getFront(compound.getInteger("facing"));
    }

    private void addPosInfo(NBTTagCompound compound) {
        if (origin == null) {
            compound.setBoolean("null", true);
        }
        else {
            compound.setBoolean("null", false);
            compound.setInteger("ox", origin.getX());
            compound.setInteger("oy", origin.getY());
            compound.setInteger("oz", origin.getZ());
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        addPosInfo(compound);
        return compound;
    }
}
