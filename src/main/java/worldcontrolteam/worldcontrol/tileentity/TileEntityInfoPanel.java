package worldcontrolteam.worldcontrol.tileentity;

import javax.annotation.Nonnull;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import worldcontrolteam.worldcontrol.api.card.ICard;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.inventory.ISlotItemFilter;
import worldcontrolteam.worldcontrol.items.ItemUpgrade;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class TileEntityInfoPanel extends TileEntity implements IItemHandler, ITickable, ISlotItemFilter, IItemHandlerModifiable, RedstoneHelper.IRedstoneConsumer  {
    public int color;
    public boolean power;

    public BlockPos origin;
    public BlockPos end;

    public IScreenElement ise;

    public EnumFacing facing;
    private NonNullList<ItemStack> itemStack = NonNullList.withSize(3, ItemStack.EMPTY);

    private Map<BlockPos, Boolean> validCache = new HashMap<>();

    public TileEntityInfoPanel() {
        this.color = 10;
        this.power = true;

        // debug debug debug todo: fixme: aaaaaaa

        this.setStackInSlot(0, new ItemStack(WCContent.TIME_CARD, 1));
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
    public int getSlots() {
        return this.itemStack.size();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.itemStack.get(i);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        //Modified from {@link SidedInvWrapper#extractItem}
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (slot == -1)
            return stack;

        ItemStack stackInSlot = this.getStackInSlot(slot);

        int m;
        if (!stackInSlot.isEmpty())
        {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
                return stack;

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
                return stack;


            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    this.itemStack.set(slot, copy);
                }

                return stack;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackInSlot.getCount());
                    this.itemStack.set(slot, copy);
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    this.itemStack.set(slot, stack.splitStack(m));
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate)
                    this.itemStack.set(slot,  stack);
                return ItemStack.EMPTY;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        //Modified from {@link SidedInvWrapper#extractItem}
        if(amount == 0)
            return ItemStack.EMPTY;

        ItemStack stack = getStackInSlot(slot);
        if(stack.isEmpty()){
            return ItemStack.EMPTY;
        }

        if (simulate)
        {
            if (stack.getCount() < amount)
            {
                return stack.copy();
            }
            else
            {
                ItemStack copy = stack.copy();
                copy.setCount(amount);
                return copy;
            }
        }
        else
        {
            int m = Math.min(stack.getCount(), amount);
            ItemStack ret = this.decrStackSize(slot, m);
            this.markDirty();
            return ret;
        }
    }

    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != ItemStack.EMPTY)
            if (stack.getCount() > amount) {
                stack = stack.splitStack(amount);
                markDirty();
            } else {
                this.itemStack.set(slot, ItemStack.EMPTY);
            }
        return stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }


    public void closeInventory(EntityPlayer entityPlayer) {
        if (this.getStackInSlot(0) == ItemStack.EMPTY) {
            ise = null;
        }
        else {
            ise = ((ICard)getCard().getItem()).getRenderer(getCard());
        }
    }

    @Override
    public void update() {
        if (getCard() != ItemStack.EMPTY) {
            ICard icard = (ICard) getCard().getItem();
            icard.update(world, getCard());
            ise.onCardUpdate(world, getCard());
        }
        RedstoneHelper.checkPowered(world, this);
    }

    private ItemStack getCard(){
        return this.getStackInSlot(0);
    }

    @Override
    public boolean isItemValid(int slotIndex, ItemStack itemStack) {
        switch (slotIndex) {
            case 0:
                return itemStack.getItem() instanceof ICard;
            case 1:
                return itemStack.getItem() instanceof ItemUpgrade && itemStack.getMetadata() == ItemUpgrade.DAMAGE_RANGE;
            default:
                return itemStack.getItem() instanceof ItemUpgrade && itemStack.getMetadata() == ItemUpgrade.DAMAGE_COLOR;
        }

    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (!isItemValid(slot, stack)) return;
        this.itemStack.set(slot, stack);
    }

    @Override
    public boolean getPowered(){
      return power;
    }

    @Override
    public void setPowered(boolean value){
      this.power = value;
    }

}
