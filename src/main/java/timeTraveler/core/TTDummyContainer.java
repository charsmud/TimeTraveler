package timeTraveler.core;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class TTDummyContainer extends DummyModContainer {

public TTDummyContainer() {

super(new ModMetadata());
ModMetadata meta = getMetadata();
meta.modId = "TimeTraveler";
meta.name = "TimeTraveler";
meta.version = "@VERSION@";
meta.credits = "Roll Credits ...";
meta.authorList = Arrays.asList("Charsmud");
meta.description = "";
meta.url = "https://github.com/culegooner/CreeperBurnCore";
meta.updateUrl = "";
meta.screenshots = new String[0];
meta.logoFile = "";

}

@Override
public boolean registerBus(EventBus bus, LoadController controller) {
bus.register(this);
return true;
}

@Subscribe
public void modConstruction(FMLConstructionEvent evt){

}

@Subscribe
public void preInit(FMLPreInitializationEvent evt) {

}

@Subscribe
public void init(FMLInitializationEvent evt) {

}


@Subscribe
public void postInit(FMLPostInitializationEvent evt) {

}

}
