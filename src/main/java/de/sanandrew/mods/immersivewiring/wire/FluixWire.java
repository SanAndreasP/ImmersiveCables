package de.sanandrew.mods.immersivewiring.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.item.ItemRegistryAE2;
import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class FluixWire
        extends WireType
{
    @Override
    public String getUniqueName() {
        return "AE2FLUIX";
    }

    @Override
    public double getLossRatio() {
        return 0;
    }

    @Override
    public int getTransferRate() {
        return 0;
    }

    @Override
    public int getColour(ImmersiveNetHandler.Connection connection) {
        return 0xD29BFF;
    }

    @Override
    public int getMaxLength() {
        return IWConfiguration.ae2FluixWireMaxLength;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemRegistryAE2.WIRE_COIL, 1, Wires.FLUIX.ordinal());
    }

    @Override
    public double getRenderDiameter() {
        return 0.06f;
    }

    @Override
    public boolean isEnergyWire() {
        return false;
    }

    @Override
    public double getSlack() {
        return 1.005;
    }

    @Override
    public TextureAtlasSprite getIcon(ImmersiveNetHandler.Connection connection) {
        return iconDefaultWire;
    }
}
