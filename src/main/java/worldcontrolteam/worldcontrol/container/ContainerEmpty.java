package worldcontrolteam.worldcontrol.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class ContainerEmpty extends Container {
    private TileEntity entity;

    public ContainerEmpty(TileEntity entity) {
        super();
        this.entity = entity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(entity.getPos().getX() + 0.5D, entity.getPos().getY() + 0.5D, entity.getPos().getZ() + 0.5D) <= 64.0D;
    }
}

