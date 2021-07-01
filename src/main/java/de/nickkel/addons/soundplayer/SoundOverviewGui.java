package de.nickkel.addons.soundplayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.labymod.gui.elements.ModTextField;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.gui.elements.Tabs;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class SoundOverviewGui extends Screen {

    private final Scrollbar scrollbar = new Scrollbar(25);
    private ModTextField fieldVolume, fieldPitch;
    private String storedVolume, storedPitch;
    private double lastScrollY;

    public SoundOverviewGui() {
        super(new StringTextComponent("SoundPlayer"));
    }

    @Override
    public void init() {
        super.init();
        Tabs.initGui(this);
        if (LabyMod.getInstance().isInGame()) {
            this.scrollbar.init();
            this.scrollbar.setPosition(this.width / 2 + 122, 44, this.width / 2 + 126,
                    this.height - 32 - 3);
            this.scrollbar.setSpeed(50);

            this.fieldVolume = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, 27, 90, 25, 20);
            this.fieldVolume.setMaxStringLength(4);
            this.fieldVolume.setText("1.0");
            this.storedVolume = "1.0";

            this.fieldPitch = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, 27, 143, 25, 20);
            this.fieldPitch.setMaxStringLength(4);
            this.fieldPitch.setText("1.0");
            this.storedPitch = "1.0";

            int buttonLength = 200;
            int buttonHeight = 20;
            int lastY = 5;
            for (int i = 0; i < Sound.values().length; i++) {
                Sound sound = Sound.values()[i];
                lastY += buttonHeight + 5;
                this.addButton(new Button(this.width / 2 - buttonLength / 2, lastY, buttonLength, buttonHeight, new StringTextComponent(sound.name()), onClick -> {
                    try {
                        SoundPlayerAddon.getInstance().playSound(sound, Float.parseFloat(this.storedVolume), Float.parseFloat(this.storedPitch));
                    } catch (Exception e) {
                        SoundPlayerAddon.getInstance().playSound(sound, 1.0F, 1.0F);
                    }
                }));
            }
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        Tabs.drawScreen(this, matrixStack, mouseX, mouseY, partialTicks);

        if (LabyMod.getInstance().isInGame()) {
            if (this.lastScrollY != this.scrollbar.getScrollY()) {
                double scrollDifference = this.scrollbar.getScrollY() - this.lastScrollY;
                this.lastScrollY = this.scrollbar.getScrollY();
                for (Widget widget : this.buttons) {
                    widget.y += scrollDifference;
                }
            }

            this.scrollbar.update(Sound.values().length);
            this.scrollbar.draw();

            LabyMod.getInstance().getDrawUtils().drawString(matrixStack, "Volume", 27, 76);
            this.fieldVolume.drawTextBox(matrixStack);

            LabyMod.getInstance().getDrawUtils().drawString(matrixStack, "Pitch", 27, 129);
            this.fieldPitch.drawTextBox(matrixStack);
        } else {
            LabyMod.getInstance().getDrawUtils().drawCenteredString(matrixStack, "§cPlease join a world or a server to use the SoundPlayer addon", this.width / 2, 100);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (LabyMod.getInstance().isInGame()) {
            if (this.fieldVolume.keyPressed(keyCode, scanCode, modifiers)) {
                this.storedVolume = this.fieldVolume.getText();
            }
            if (this.fieldPitch.keyPressed(keyCode, scanCode, modifiers)) {
                this.storedPitch = this.fieldPitch.getText();
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (LabyMod.getInstance().isInGame()) {
            if (this.fieldVolume.textboxKeyTyped(codePoint, modifiers)) {
                this.storedVolume = this.fieldVolume.getText();
            }
            if (this.fieldPitch.textboxKeyTyped(codePoint, modifiers)) {
                this.storedPitch = this.fieldPitch.getText();
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Tabs.mouseClicked(this);
        if (LabyMod.getInstance().isInGame()) {
            this.scrollbar.mouseAction((int) mouseX, (int) mouseY, Scrollbar.EnumMouseAction.CLICKED);
            this.fieldVolume.mouseClicked((int) mouseX, (int) mouseY, button);
            this.fieldPitch.mouseClicked((int) mouseX, (int) mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        this.scrollbar.mouseAction((int) mouseX, (int) mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        this.scrollbar.mouseAction((int) mouseX, (int) mouseY, Scrollbar.EnumMouseAction.RELEASED);
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.scrollbar.mouseInput(delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}