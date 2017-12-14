/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.block;

import appeng.api.AEApi;
import appeng.api.util.AEPartLocation;
import appeng.client.UnlistedProperty;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityTransformerFluix;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;

public class BlockTransformerFluix
        extends BlockDirectional
{
    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockTransformerFluix() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(IWConstants.ID + ":transformer_fluix");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Type.FLUIX).withProperty(FACING, EnumFacing.UP));
        this.setRegistryName(IWConstants.ID, "transformer_fluix");
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTransformerFluix();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, Type.VALUES[meta & 1])
                                     .withProperty(FACING, EnumFacing.VALUES[(meta >> 1) & 7]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(TYPE).ordinal() & 1) | ((state.getValue(FACING).getIndex() & 7) << 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, FACING, ACTIVE);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(ACTIVE, isMeActive(world, pos));
    }

    private static boolean isMeActive(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityTransformerFluix && ((TileEntityTransformerFluix) te).isMeActive();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if( !worldIn.isRemote ) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if( tile instanceof TileEntityTransformerFluix ) {
                ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(tile), worldIn, new TargetingInfo(EnumFacing.UP, 0.0F, 0.0F, 0.0F));
                IESaveData.setDirty(worldIn.provider.getDimension());
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        return getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if( !world.isRemote && placer instanceof EntityPlayer ) {
            TileEntity transformer = world.getTileEntity(pos);
            if( transformer instanceof TileEntityTransformerFluix ) {
                ((TileEntityTransformerFluix) transformer).getGridNode(AEPartLocation.INTERNAL).setPlayerID(AEApi.instance().registries().players().getID((EntityPlayer) placer));
            }
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for( int i = 0; i < Type.VALUES.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public enum Type
            implements IStringSerializable
    {
        FLUIX, FLUIX_DENSE;

        public static final Type[] VALUES = values();

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
}
