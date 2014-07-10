package timeTraveler.gui;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.stats.StatList;
import net.minecraft.world.WorldSettings;

import org.lwjgl.input.Keyboard;

import timeTraveler.core.TimeTraveler;
import timeTraveler.mechanics.CopyFile;

/**
 * GUI for returning to the Present from the Future
 * @author Charsmud
 *
 */
public class GuiFutureReturn extends GuiScreen
{
    public GuiFutureReturn()
    {
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
    }
	public boolean doesGuiPauseGame()
	{
		return true;
	}
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
	public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, ("Yes")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, ("No")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 0)
            {
            	File present = new File(mc.mcDataDir + "/mods/TimeMod/present/" + mc.getIntegratedServer().getWorldName());
            	File future = new File(mc.mcDataDir + "/mods/TimeMod/future/" + mc.getIntegratedServer().getWorldName() + "/" + TimeTraveler.vars.getFuture());
		        File worldFile = new File(mc.mcDataDir + "/saves/" + mc.getIntegratedServer().getWorldName() + "/region");
            	
				String worldName = mc.getIntegratedServer().getWorldName();
				String folderName = mc.getIntegratedServer().getFolderName();
		     
            	try 
            	{
    		        mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
    		        mc.theWorld.sendQuittingDisconnectingPacket();
    		        mc.loadWorld((WorldClient)null);

            		Thread.sleep(3000);
					CopyFile.moveMultipleFiles(worldFile, future);
					Thread.sleep(2000);
					CopyFile.moveMultipleFiles(present, worldFile);
				} 
            	catch (Exception e)
            	{
					e.printStackTrace();
					System.out.println("Couldn't move files!");
				}
            	TimeTraveler.vars.setIsInFuture(false);
		        mc.launchIntegratedServer(folderName, worldName, (WorldSettings)null);

            	//mc.displayGuiScreen(null);
            }
            else if (par1GuiButton.id == 1)
            {
                this.mc.displayGuiScreen(null);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, ("Return to the Present?"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}
