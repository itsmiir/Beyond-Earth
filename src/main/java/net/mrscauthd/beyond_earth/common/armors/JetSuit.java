package net.mrscauthd.beyond_earth.common.armors;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.mrscauthd.beyond_earth.BeyondEarth;
import net.mrscauthd.beyond_earth.common.capabilities.oxygen.IOxygenStorage;
import net.mrscauthd.beyond_earth.common.capabilities.oxygen.OxygenCapability;
import net.mrscauthd.beyond_earth.common.capabilities.oxygen.OxygenProvider;
import net.mrscauthd.beyond_earth.client.entities.renderers.armors.JetSuitModel;
import net.mrscauthd.beyond_earth.common.keybinds.KeyVariables;
import net.mrscauthd.beyond_earth.common.util.Methods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class JetSuit {

    public static class OxygenMask extends ArmorItem {
        public OxygenMask(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
            super(p_40386_, p_40387_, p_40388_);
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {

                @Override
                public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {

                    Map<String, ModelPart> map = Map.of("head", new JetSuitModel.JET_SUIT_P1(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P1.LAYER_LOCATION)).head,

                            "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap())
                    );

                    ModelPart modelPart = new ModelPart(Collections.emptyList(), map);
                    JetSuitModel.JET_SUIT_P1 armorModel = new JetSuitModel.JET_SUIT_P1(modelPart);

                    armorModel.entity = livingEntity;

                    return armorModel;
                }
            });
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return BeyondEarth.MODID + ":textures/armor/jet_suit_oxygen_mask.png";
        }
    }

    public static class Suit extends ArmorItem {

        public static String TAG_MODE = BeyondEarth.MODID + ":jet_suit_mode";

        public Suit(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
            super(p_40386_, p_40387_, p_40388_);
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {

                @Override
                public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {

                    Map<String, ModelPart> map = Map.of(
                            "body", new JetSuitModel.JET_SUIT_P1(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P1.LAYER_LOCATION)).body,
                            "right_arm", new JetSuitModel.JET_SUIT_P1(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P1.LAYER_LOCATION)).rightArm,
                            "left_arm", new JetSuitModel.JET_SUIT_P1(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P1.LAYER_LOCATION)).leftArm,

                            "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap())
                    );

                    ModelPart modelPart = new ModelPart(Collections.emptyList(), map);
                    JetSuitModel.JET_SUIT_P1 armorModel = new JetSuitModel.JET_SUIT_P1(modelPart);

                    armorModel.entity = livingEntity;

                    return armorModel;
                }
            });
        }

        public enum ModeType {
            DISABLED(Component.translatable("general." + BeyondEarth.MODID + ".jet_suit_disabled_mode"), ChatFormatting.RED, 0),
            NORMAL(Component.translatable("general." + BeyondEarth.MODID + ".jet_suit_normal_mode"), ChatFormatting.GREEN, 1),
            HOVER(Component.translatable("general." + BeyondEarth.MODID + ".jet_suit_hover_mode"), ChatFormatting.GREEN, 2),
            ELYTRA(Component.translatable("general." + BeyondEarth.MODID + ".jet_suit_elytra_mode"), ChatFormatting.GREEN, 3);

            private int mode;
            private ChatFormatting chatFormatting;
            private Component component;

            ModeType(Component component, ChatFormatting chatFormatting, int mode) {
                this.mode = mode;
                this.chatFormatting = chatFormatting;
                this.component = component;
            }

            public int getMode() {
                return mode;
            }

            public ChatFormatting getChatFormatting() {
                return chatFormatting;
            }

            public Component getTranslationKey() {
                return component;
            }
        }

        public void switchJetSuitMode(Player player, ItemStack itemStack) {
            CompoundTag compoundTag = itemStack.getOrCreateTag();

            if (compoundTag.getInt(JetSuit.Suit.TAG_MODE) < 3) {
                compoundTag.putInt(JetSuit.Suit.TAG_MODE, compoundTag.getInt(JetSuit.Suit.TAG_MODE) + 1);
            } else {
                compoundTag.putInt(JetSuit.Suit.TAG_MODE, 0);
            }
        }

        @Override
        public void onArmorTick(ItemStack stack, Level level, Player player) {
            /** OXYGEN SYSTEM */
            Methods.extractArmorOxygenUsingTimer(stack, player);

            /** NORMAL FLY MOVEMENT */
            this.normalFlyModeMovement(player, stack);
        }

        public void normalFlyModeMovement(Player player, ItemStack stack) {
            if (!player.getAbilities().flying) {

                /** HOVER FLY */
                if (stack.getOrCreateTag().getInt(TAG_MODE) == ModeType.HOVER.getMode()) {
                    double gravity = player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getBaseValue();

                    /** MOVE UP */
                    if (KeyVariables.isHoldingJump(player)) {
                        player.moveRelative(1.2F, new Vec3(0, 0.1, 0));
                        player.resetFallDistance();
                    }

                    /** MOVE DOWN */
                    if (player.isCrouching()) {
                        player.moveRelative(1.2F, new Vec3(0, -0.1, 0));
                    }

                    /** MOVE FORWARD AND BACKWARD */
                    if (!player.isOnGround()) {
                        if (KeyVariables.isHoldingUp(player)) {
                            player.moveRelative(1, new Vec3(0, 0, 0.03));
                        }
                        else if (KeyVariables.isHoldingDown(player)) {
                            player.moveRelative(1, new Vec3(0, 0, -0.03));
                        }
                    }

                    /** MOVE SIDEWAYS */
                    if (!player.isOnGround()) {
                        if (KeyVariables.isHoldingRight(player)) {
                            player.moveRelative(1, new Vec3(-0.03, 0, 0));
                        }
                        else if (KeyVariables.isHoldingLeft(player)) {
                            player.moveRelative(1, new Vec3(0.03, 0, 0));
                        }
                    }
                }

                /** NORMAL FLY */
                if (stack.getOrCreateTag().getInt(TAG_MODE) == ModeType.NORMAL.getMode()) {
                    /** MOVE UP */
                    if (KeyVariables.isHoldingJump(player)) {
                        player.moveRelative(1.2F, new Vec3(0, 0.1, 0));
                        player.resetFallDistance();
                    }

                    /** MOVE FORWARD AND BACKWARD */
                    if (!player.isOnGround()) {
                        if (KeyVariables.isHoldingUp(player)) {
                            player.moveRelative(1, new Vec3(0, 0, 0.03));
                        }
                        else if (KeyVariables.isHoldingDown(player)) {
                            player.moveRelative(1, new Vec3(0, 0, -0.03));
                        }
                    }

                    /** MOVE SIDEWAYS */
                    if (!player.isOnGround()) {
                        if (KeyVariables.isHoldingRight(player)) {
                            player.moveRelative(1, new Vec3(-0.03, 0, 0));
                        }
                        else if (KeyVariables.isHoldingLeft(player)) {
                            player.moveRelative(1, new Vec3(0.03, 0, 0));
                        }
                    }
                }
            }
        }

        @Override
        public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
            return Methods.isLivingInJetSuit(entity) && stack.getOrCreateTag().getInt(TAG_MODE) == ModeType.ELYTRA.getMode();
        }

        @Override
        public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
            if (!entity.level.isClientSide) {
                int nextFlightTick = flightTicks + 1;
                if (nextFlightTick % 10 == 0) {
                    entity.gameEvent(GameEvent.ELYTRA_GLIDE);
                }
            }

            return true;
        }

        @Override
        public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
            super.fillItemCategory(p_41391_, p_41392_);
            if (this.allowedIn(p_41391_)) {
                ItemStack itemStack = new ItemStack(this);
                IOxygenStorage oxygenStorage = itemStack.getCapability(OxygenCapability.OXYGEN).orElse(null);
                if (oxygenStorage != null) {
                    oxygenStorage.receiveOxygen(oxygenStorage.getMaxOxygenStored(), false);
                }

                p_41392_.add(itemStack);
            }
        }

        @Override
        public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
            return new OxygenProvider(48000);
        }

        @Override
        public void appendHoverText(ItemStack p_41421_, Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
            IOxygenStorage oxygenStorage = p_41421_.getCapability(OxygenCapability.OXYGEN).orElse(null);
            if (oxygenStorage != null) {
                p_41423_.add(Component.translatable("general." + BeyondEarth.MODID + ".oxygen").append(": ").withStyle(ChatFormatting.BLUE).append("\u00A76" + oxygenStorage.getOxygenStored() + " mb" +  "\u00A78" + " | " + "\u00A7c" + oxygenStorage.getMaxOxygenStored() + " mb"));
            }
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return BeyondEarth.MODID + ":textures/armor/jet_suit.png";
        }
    }

    public static class Pants extends ArmorItem {
        public Pants(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
            super(p_40386_, p_40387_, p_40388_);
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {

                @Override
                public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {

                    Map<String, ModelPart> map = Map.of(
                            "right_leg", new JetSuitModel.JET_SUIT_P2(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P2.LAYER_LOCATION)).rightLeg,
                            "left_leg", new JetSuitModel.JET_SUIT_P2(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P2.LAYER_LOCATION)).leftLeg,

                            "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap())

                    );

                    ModelPart modelPart = new ModelPart(Collections.emptyList(), map);
                    JetSuitModel.JET_SUIT_P2 armorModel = new JetSuitModel.JET_SUIT_P2(modelPart);

                    armorModel.entity = livingEntity;

                    return armorModel;
                }

            });
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return BeyondEarth.MODID + ":textures/armor/jet_suit_pants.png";
        }
    }

    public static class Boots extends ArmorItem {
        public Boots(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
            super(p_40386_, p_40387_, p_40388_);
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {

                @Override
                public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                    Map<String, ModelPart> map = Map.of(
                            "right_leg", new JetSuitModel.JET_SUIT_P1(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P1.LAYER_LOCATION)).rightLeg,
                            "left_leg", new JetSuitModel.JET_SUIT_P1(Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.JET_SUIT_P1.LAYER_LOCATION)).leftLeg,

                            "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                            "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap())

                    );

                    ModelPart modelPart = new ModelPart(Collections.emptyList(), map);
                    JetSuitModel.JET_SUIT_P1 armorModel = new JetSuitModel.JET_SUIT_P1(modelPart);

                    armorModel.entity = livingEntity;

                    return armorModel;
                }
            });
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return BeyondEarth.MODID + ":textures/armor/jet_suit.png";
        }
    }
}
