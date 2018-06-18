package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.api.card.ICard;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class TileEntityInfoPanel extends TileEntity implements IInventory, ITickable {
    public int color;
    public boolean power;

    public BlockPos origin;
    public BlockPos end;

    public ItemStack card;
    public IScreenElement ise;

    public EnumFacing facing;

    private Map<BlockPos, Boolean> validCache = new HashMap<>();

    public TileEntityInfoPanel() {
        this.color = 10;
        this.power = true;

        // debug debug debug todo: fixme: aaaaaaa

        card = new ItemStack(WCContent.TIME_CARD, 1);
        closeInventory(null);
    }

    public void init() {
        this.facing = (EnumFacing) world.getBlockState(getPos()).getProperties().get(BlockInfoPanel.FACING);
        if (origin == null) origin = getPos();
        if (end == null) end = getPos();

        searchForNeighbours();
        updateAllProviders(false);
    }

    public void reInit() {
        updateAllProviders(true);
        searchForNeighbours();
        updateAllProviders(false);
    }

    private boolean isAreaValid(BlockPos o, BlockPos e) {
        for (int x = o.getX(); x <= e.getX(); x++) {
            for (int y = o.getY(); y <= e.getY(); y++) {
                for (int z = o.getZ(); z <= e.getZ(); z++) {
                    final BlockPos pos1 = new BlockPos(x, y, z);
                    if (validCache.containsKey(pos1)) {
                        if (!validCache.get(pos1)) return false;
                        continue;
                    }
                    if (!(world.getBlockState(pos1).getBlock() instanceof IScreenContainer)) {
                        validCache.put(pos1, false);
                        return false;
                    }
                    IScreenContainer c = (IScreenContainer) world.getBlockState(pos1).getBlock();
                    if (!(c.getFacing(world, pos1) == facing)) {
                        validCache.put(pos1, false);
                        return false;
                    }
                    if (!c.isValid(world, pos1)) {
                        validCache.put(pos1, false);
                        return false;
                    }
                    if (c.getOrigin(world, pos1) != null && !WCUtility.compareBPos(c.getOrigin(world, pos1), getPos())) {
                        validCache.put(pos1, false);
                        return false;
                    }
                    validCache.put(pos1, true);
                }
            }
        }
        return true;
    }
    
    public void updateAllProviders(boolean nullify) {
        for (int x = origin.getX(); x <= end.getX(); x++) {
            for (int y = origin.getY(); y <= end.getY(); y++) {
                for (int z = origin.getZ(); z <= end.getZ(); z++) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() instanceof IScreenContainer) {
                        if (nullify) {
                            ((IScreenContainer) (world.getBlockState(pos).getBlock())).setOrigin(world, pos, null);
                        }
                        else {
                            ((IScreenContainer) (world.getBlockState(pos).getBlock())).setOrigin(world, pos, getPos());
                        }
                    }
                }
            }
        }
    }


    private boolean inSamePlane(BlockPos c) {
        switch (facing) {
            case UP:
            case DOWN:
                return c.getY() == origin.getY();
            case NORTH:
            case SOUTH:
                return c.getZ() == origin.getZ();
            default:
                return c.getX() == origin.getX();
        }
    }

    public void tryToAdd(BlockPos aPos) {
        if (!inSamePlane(aPos)) {
            return;
        }
        updateAllProviders(true);
        searchForNeighbours();
        updateAllProviders(false);
    }

    private void searchForNeighbours() {
        validCache = new HashMap<>();
        EnumFacing left = EnumFacing.WEST; // negRelativeX
        EnumFacing down = EnumFacing.DOWN; // negRealtiveY

        switch (facing) {
            case UP:
            case DOWN:
                left = EnumFacing.WEST;
                down = EnumFacing.NORTH;
                break;
            case EAST:
            case WEST:
                left = EnumFacing.NORTH;
                down = EnumFacing.DOWN;
                break;
            case NORTH:
            case SOUTH:
                left = EnumFacing.WEST;
                down = EnumFacing.DOWN;
                break;
        }
        if (!isAreaValid(origin, end)) {
            origin = getPos();
            end = getPos();
        }
        int n = 0;
        do {
            origin = origin.offset(left);
            n += 1;
        } while (isAreaValid(origin, end.offset(left, n)));


        n = 0;

        origin = origin.offset(left.getOpposite());

        do {
            origin = origin.offset(down);
            n += 1;
        } while (isAreaValid(origin, end.offset(down, n)));


        origin = origin.offset(down.getOpposite());
        n = 0;

        do {
            end = end.offset(left.getOpposite());
            n += 1;
        } while (isAreaValid(origin.offset(left.getOpposite(), n), end));


        end = end.offset(left);
        n = 0;

        do {
            end = end.offset(down.getOpposite());
            n += 1;
        } while (isAreaValid(origin.offset(down.getOpposite(), n), end));


        end = end.offset(down);
        IBlockState bs = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), bs, bs, 0);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        if (origin == null) origin = getPos();
        if (end == null) end = getPos();
        if (facing == null) facing = EnumFacing.NORTH;

        compound.setInteger("oX", origin.getX());
        compound.setInteger("oY", origin.getY());
        compound.setInteger("oZ", origin.getZ());

        compound.setInteger("eX", end.getX());
        compound.setInteger("eY", end.getY());
        compound.setInteger("eZ", end.getZ());

        compound.setInteger("facing", facing.getIndex());
        compound.setBoolean("power", power);
        compound.setInteger("color", color);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        origin = new BlockPos(
                compound.getInteger("oX"),
                compound.getInteger("oY"),
                compound.getInteger("oZ"));

        end = new BlockPos(
                compound.getInteger("eX"),
                compound.getInteger("eY"),
                compound.getInteger("eZ"));

        facing = EnumFacing.getFront(compound.getInteger("facing"));
        power = compound.getBoolean("power");
        color = compound.getInteger("color");

        //updateAllProviders(false);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setInteger("oX", origin.getX());
        compound.setInteger("oY", origin.getY());
        compound.setInteger("oZ", origin.getZ());

        compound.setInteger("eX", end.getX());
        compound.setInteger("eY", end.getY());
        compound.setInteger("eZ", end.getZ());

        compound.setInteger("facing", facing.getIndex());
        compound.setBoolean("power", power);
        compound.setInteger("color", color);

        return new SPacketUpdateTileEntity(getPos(), 3, compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();

        compound.setInteger("oX", origin.getX());
        compound.setInteger("oY", origin.getY());
        compound.setInteger("oZ", origin.getZ());

        compound.setInteger("eX", end.getX());
        compound.setInteger("eY", end.getY());
        compound.setInteger("eZ", end.getZ());

        compound.setInteger("facing", facing.getIndex());
        compound.setBoolean("power", power);
        compound.setInteger("color", color);

        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.getNbtCompound();

        origin = new BlockPos(
                compound.getInteger("oX"),
                compound.getInteger("oY"),
                compound.getInteger("oZ"));

        end = new BlockPos(
                compound.getInteger("eX"),
                compound.getInteger("eY"),
                compound.getInteger("eZ"));

        facing = EnumFacing.getFront(compound.getInteger("facing"));
        power = compound.getBoolean("power");
        color = compound.getInteger("color");
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return card == null;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return card;
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        if (card != null && i1 > 0) {
            this.card = null;
        }
        return this.card;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if (!isItemValidForSlot(0, itemStack)) return;
        this.card = itemStack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        if (this.card == null) {
            ise = null;
        }
        else {
            ise = ((ICard)this.card.getItem()).getRenderer(this.card);
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return itemStack.getItem() instanceof ICard;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        card = null;
    }

    @Override
    public String getName() {
        return "Info Panel";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void update() {
        if (this.card != null) {
            ICard icard = (ICard) this.card.getItem();
            icard.update(world, this.card);
            ise.onCardUpdate(world, this.card);
        }
    }
}
