package de.nickkel.addons.soundplayer;

import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.Tabs;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.List;

public class SoundPlayerAddon extends LabyModAddon {

  private static SoundPlayerAddon instance;

  @Override
  public void onEnable() {
    instance = this;
    Tabs.registerTab("Sounds", SoundOverviewGui.class);
  }

  @Override
  public void loadConfig() {

  }

  @Override
  protected void fillSettings(List<SettingsElement> list) {

  }

  public void playSound(Sound sound, float volume, float pitch) {
    ClientPlayerEntity player = LabyModCore.getMinecraft().getPlayer();
    playSound(sound, player.getPosX(), player.getPosY(), player.getPosZ(), volume, pitch);
  }

  private void playSound(Sound sound, double soundX, double soundY, double soundZ, float volume, float pitch) {
    SPlaySoundEffectPacket packetSoundEffect = new SPlaySoundEffectPacket(new SoundEvent(sound.getResourceLocation()), SoundCategory.MASTER, soundX, soundY, soundZ, volume, pitch);
    LabyModCore.getMinecraft().getConnection().handleSoundEffect(packetSoundEffect);
  }

  public static SoundPlayerAddon getInstance() {
    return instance;
  }
}