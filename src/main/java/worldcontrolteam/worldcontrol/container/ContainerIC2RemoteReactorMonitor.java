package worldcontrolteam.worldcontrol.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.TileEntityIC2RemoteReactorMonitor;
import worldcontrolteam.worldcontrol.inventory.SlotFilter;
import worldcontrolteam.worldcontrol.inventory.SlotFilterItemHandler;

import java.util.List;

public class ContainerIC2RemoteReactorMonitor extends Container {

    private EntityPlayer player;
    private TileEntityIC2RemoteReactorMonitor remoteHeatMonitor;
    private double lastEnergy = -1;

    public ContainerIC2RemoteReactorMonitor(EntityPlayer player, TileEntityIC2RemoteReactorMonitor remoteThermo) {
        super();

        this.player = player;
        this.remoteHeatMonitor = remoteThermo;

        //energy charger
        addSlotToContainer(new SlotFilterItemHandler(remoteThermo, 1, 13, 53));

        //upgrades
        addSlotToContainer(new SlotFilterItemHandler(remoteThermo, 0, 190, 8));
        addSlotToContainer(new SlotFilterItemHandler(remoteThermo, 2, 190, 26));
        addSlotToContainer(new SlotFilterItemHandler(remoteThermo, 3, 190, 44));
        addSlotToContainer(new SlotFilterItemHandler(remoteThermo, 4, 190, 62));

        //inventory
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 27 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(player.inventory, j, 27 + j * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int energy = (int) remoteHeatMonitor.getEnergy();
        for (int i = 0; i < this.listeners.size(); i++) {
            IContainerListener crafting = (IContainerListener) this.listeners.get(i);
            if (lastEnergy != energy) {
                crafting.sendWindowProperty(this, 0, energy);
            }
        }
        lastEnergy = energy;
    }

    public void updateProgressBar(int type, int value) {
        if (type == 0) {
            remoteHeatMonitor.setEnergy(value);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return true;
    }
}
