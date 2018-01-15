package worldcontrolteam.worldcontrol.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;
import worldcontrolteam.worldcontrol.utils.WCConfig;
import worldcontrolteam.worldcontrol.utils.WCUtility;


public class TileEntityHowlerAlarm extends TileEntity implements ITickable, RedstoneHelper.IRedstoneConsumer {

    private static final String DEFAULT_SOUND_NAME = "default";
    private static final String SOUND_PREFIX = "worldcontrol:alarm-";
    private static final float BASE_SOUND_RANGE = 16F;
    protected int tickRate;
    private String soundName;
    private int color;
    private int range;
    private int updateTicker;
    private boolean init = false;
    private boolean powered;
    @SideOnly(Side.CLIENT)
    private TileEntitySound sound;


    public TileEntityHowlerAlarm() {
        tickRate = 2;
        updateTicker = 0;
        soundName = DEFAULT_SOUND_NAME;
        range = WCConfig.alarmRange;
        color = WCUtility.WHITE;
        if (WorldControl.SIDE == Side.CLIENT) {
            sound = new TileEntitySound(BASE_SOUND_RANGE);
        }
    }

    @Override
    public boolean getPowered() {
        return powered;
    }

    @Override
    public void setPowered(boolean value) {
        powered = value;

        if (powered) {
            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                sound.playAlarm(getPos().add(0.5, 0.5, 0.5), SOUND_PREFIX + soundName, getNormalizedRange(), false);
        } else {
            if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                sound.stopAlarm();
            }
        }
    }

    @Override
    public void update() {
        // WCUtility.error(color);
        if (!init) {
            if (!getWorld().isRemote)
                RedstoneHelper.checkPowered(getWorld(), this);
            init = true;
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (tickRate != -1 && updateTicker-- > 0)
                return;
            updateTicker = tickRate;
            checkStatus();
        }
    }

    protected void checkStatus() {
        RedstoneHelper.checkPowered(getWorld(), this);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (powered && !sound.isPlaying()) {
                sound.playAlarm(this.getPos().add(0.5, 0.5, 0.5), SOUND_PREFIX + soundName, getNormalizedRange(), true);
            }
        }
    }

    private float getNormalizedRange() {
        if (getWorld().isRemote) {
            return Math.min(range, WCConfig.SMPmaxAlarmRange) / BASE_SOUND_RANGE;
        }
        return range / BASE_SOUND_RANGE;
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        syncData.setString("soundName", soundName);
        syncData.setInteger("range", range);
        syncData.setInteger("color", color);
        syncData.setBoolean("powered", powered);
        return new SPacketUpdateTileEntity(this.getPos(), 1, syncData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(net.minecraft.network.NetworkManager net, SPacketUpdateTileEntity pkt) {
        color = pkt.getNbtCompound().getInteger("color");
        range = pkt.getNbtCompound().getInteger("range");
        soundName = pkt.getNbtCompound().getString("soundName");
        powered = pkt.getNbtCompound().getBoolean("powered");
    }


    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        color = nbttagcompound.getInteger("color");
        if (nbttagcompound.hasKey("soundName")) {
            soundName = nbttagcompound.getString("soundName");
            range = nbttagcompound.getInteger("range");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("soundName", soundName);
        nbttagcompound.setInteger("range", range);
        nbttagcompound.setInteger("color", color);
        return nbttagcompound;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getSound() {
        return soundName;
    }

    public void setSound(String sound) {
        if (Lists.newArrayList(WCConfig.howlerAlarmSounds).contains(sound)) {
            this.soundName = sound;
        } else {
            this.soundName = DEFAULT_SOUND_NAME;
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    @SideOnly(Side.CLIENT)
    public class TileEntitySound {
        private PositionedSoundRecord sound;
        private float DEFAULT_RANGE;

        public TileEntitySound(float ranger) {
            DEFAULT_RANGE = ranger;
        }

        public void stopAlarm() {
            if (sound != null) {
                Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
                sound = null;
            }
        }

        public void playAlarm(BlockPos pos, String soundName, float range, boolean skipCheck) {
            if (sound == null || skipCheck) {
                sound = playSound(pos.getX(), pos.getY(), pos.getZ(), soundName, range);
            }
        }

        public PositionedSoundRecord playSound(double x, double y, double z, String name, float volume) {
            float range = DEFAULT_RANGE;

            if (volume > 1.0F) {
                range *= volume;
            }

            Entity person = FMLClientHandler.instance().getClient().getRenderViewEntity();

            if (person != null && volume > 0 && person.getDistanceSq(x, y, z) < range * range) {
                PositionedSoundRecord sound = new PositionedSoundRecord(new SoundEvent(new ResourceLocation(name)), SoundCategory.BLOCKS, volume, 1.0F, (float) x, (float) y, (float) z);
                Minecraft.getMinecraft().getSoundHandler().playSound(sound);
                return sound;
            }
            return null;
        }

        public boolean isPlaying() {
            if (sound == null) {
                return false;
            }
            return Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(sound);
        }
    }
}
