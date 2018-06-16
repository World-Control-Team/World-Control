package worldcontrolteam.worldcontrol.screen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public interface IScreenElement extends INBTSerializable<NBTTagCompound> {
    @SideOnly(Side.CLIENT)
    void draw(); // todo: fix my syntax
}
