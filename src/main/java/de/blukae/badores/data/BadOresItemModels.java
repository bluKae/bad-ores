package de.blukae.badores.data;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Fleesonsite;
import de.blukae.badores.ore.Marmite;
import de.blukae.badores.ore.Nosleeptonite;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BadOresItemModels extends ItemModelProvider {

    public BadOresItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BadOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (BadOre ore : BadOre.values()) {
            if (ore.template.hasCustomModels()) {
                ore.template.buildCustomItemModels(this);
                continue;
            }

            if (ore.rawIngot != null) {
                basicItem(ore.rawIngot.get());
            }
            if (ore.ingot != null) {
                basicItem(ore.ingot.get());
            }

            if (ore.armor != null) {
                basicItem(ore.armor.helmet.get());
                basicItem(ore.armor.chestplate.get());
                basicItem(ore.armor.leggings.get());
                basicItem(ore.armor.boots.get());
            }
            if (ore.tools != null) {
                handheldItem(ore.tools.axe.get());
                handheldItem(ore.tools.hoe.get());
                handheldItem(ore.tools.pickaxe.get());
                handheldItem(ore.tools.shovel.get());
                handheldItem(ore.tools.sword.get());
            }
        }

        basicItem(BadOres.BAD_ORE_BOOK_ITEM.get());
        basicItem(Marmite.MARMITE_BREAD_ITEM.get());

        spawnEggItem(Fleesonsite.FLEESONSITE_SPAWN_EGG.get());
        spawnEggItem(Fleesonsite.DEEPSLATE_FLEESONSITE_SPAWN_EGG.get());
        spawnEggItem(Nosleeptonite.NOSLEEPTONITE_SPAWN_EGG.get());
    }
}
