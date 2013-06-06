package timeTraveler.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import timeTraveler.container.ContainerParadox;
import timeTraveler.tileentity.TileEntityParadoxCondenser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiParadoxCondenser extends GuiContainer
{
    private TileEntityParadoxCondenser paradoxInventory;

    public GuiParadoxCondenser(InventoryPlayer par1InventoryPlayer, TileEntityParadoxCondenser par2TileEntityFurnace)
    {
        super(new ContainerParadox(par1InventoryPlayer, par2TileEntityFurnace));
        this.paradoxInventory = par2TileEntityFurnace;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String s = this.paradoxInventory.isInvNameLocalized() ? this.paradoxInventory.getInvName() : StatCollector.translateToLocal(this.paradoxInventory.getInvName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/textures/gui/paradoxcondenser.png");
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;
        if (this.paradoxInventory.isBurning())
        {
            i1 = this.paradoxInventory.getBurnTimeRemainingScaled(12);
            //this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.paradoxInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 62, l + 15, 180, 6, 21, i1);
    }	
}