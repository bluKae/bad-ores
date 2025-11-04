/*
 * Copyright (C) 2025 bluKae
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.blukae.badores.data;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Fleesonsite;
import de.blukae.badores.ore.Marmite;
import de.blukae.badores.ore.Nosleeptonite;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class BadOresTranslations extends LanguageProvider {
    public BadOresTranslations(PackOutput output) {
        super(output, BadOres.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (BadOre ore : BadOre.values()) {
            String translation = ore.template.getTranslation(ore.name);
            add("badores." + ore.name + ".name", translation);

            addBlock(ore.oreBlock, translation + " Ore");

            if (ore.deepslateOreBlock != null) {
                addBlock(ore.deepslateOreBlock, "Deepslate " + translation + " Ore");
            }

            if (ore.rawIngotBlock != null) {
                addBlock(ore.rawIngotBlock, "Block of Raw " + translation);
            }

            if (ore.ingotBlock != null) {
                addBlock(ore.ingotBlock, "Block of " + translation);
            }

            if (ore.rawIngot != null) {
                addItem(ore.rawIngot, "Raw " + translation);
            }

            if (ore.ingot != null) {
                addItem(ore.ingot, ore.template.getIngotTranslation(translation));
            }

            if (ore.armor != null) {
                addItem(ore.armor.helmet, translation + " Helmet");
                addItem(ore.armor.chestplate, translation + " Chestplate");
                addItem(ore.armor.leggings, translation + " Leggings");
                addItem(ore.armor.boots, translation + " Boots");
            }

            if (ore.tools != null) {
                addItem(ore.tools.axe, translation + " Axe");
                addItem(ore.tools.hoe, translation + " Hoe");
                addItem(ore.tools.pickaxe, translation + " Pickaxe");
                addItem(ore.tools.shovel, translation + " Shovel");
                addItem(ore.tools.sword, translation + " Sword");
            }
        }

        add("itemGroup.badores", "Bad Ores");

        addItem(Marmite.MARMITE_BREAD_ITEM, "Marmite Bread");
        addItem(BadOres.BAD_ORE_BOOK_ITEM, "Book of Ums and Ites");

        addEntityType(Fleesonsite.FLEESONSITE_ENTITY_TYPE, "Fleesonite");
        addEntityType(Nosleeptonite.NOSLEEPTONITE_ENTITY_TYPE, "Nosleeptonite");

        addItem(Fleesonsite.FLEESONSITE_SPAWN_EGG, "Fleesonsite Spawn Egg");
        addItem(Fleesonsite.DEEPSLATE_FLEESONSITE_SPAWN_EGG, "Deepslate Fleesonsite Spawn Egg");
        addItem(Nosleeptonite.NOSLEEPTONITE_SPAWN_EGG, "Nosleeptonite Spawn Egg");

        add("death.attack.wannafite", "%1$s wants to fite!");
        add("death.attack.killium", "%1$s mined the wrong ore!");

        addRandom(
                "badores.polite.mined",
                "Thanks for mining me!",
                "That was very kind of you.",
                "Thank you very much" + ".",
                "It was a pleasure to be mined!",
                "I am jubilant to be mined by you!",
                "Being mined is amazing, " + "thank you so much!",
                "Thank you for your kindness!",
                "Hey, finally I am mined! Thanks!",
                "Thanks " + "dude.",
                "I hope mining me pleased you!",
                "Please, mine me again!",
                "Thanks!",
                ":D",
                "Never has " + "getting a pickaxe in my face felt better!",
                "Finally, free! Thank you!",
                "I am very grateful for " + "being mined today.");

        addRandom(
                "badores.polite.attack",
                "Thank you for attacking with me!",
                "Thanks for being the aggressor in " + "this battle!",
                "Oh yeah, this swing was great!",
                "Yay!",
                "Attacking feels so nice!",
                "More! More!",
                "I love attacking!",
                "Zang! Boom! Pow!",
                "Clang! Ding! Zack!",
                "*Swoosh*",
                "Thank you so much for " + "this hit!",
                "I am so greatful for this slash!",
                "Please attack more, man!");

        addRandom(
                "badores.polite.defend",
                "I am glad to be your protector!",
                "I am overjoyed to let you attack me!",
                "I'll be between you and your enemy anytime!",
                "Ouch! That was a good attack! But I held it!",
                "Please, have people attack me more!",
                "Another swing stopped professionally by me, your favourite " + "armor!",
                "Thank you for your continued trust.",
                "Even though that hurt, I am just glad I " + "could protect you.",
                "Please, don't get into dangerous situations, okay?");

        addRandom(
                "badores.polite.tool",
                "Thanks for using me!",
                "I love serving you!",
                "Please use me more!",
                "Please, harvest more blocks with me!",
                "Hey, that one felt great!",
                "I love harvesting with you.",
                "I think this was the best performance yet!",
                "Thanks for letting me mine this block!",
                "Thank you " + "for your continued use!");

        addRandom("badores.crashium.nocrash", "Poof!", "Brzzz...", "SSssssss...", "Fizzle...", "Brrrrttt...");

        addRandom(
                "badores.crashium.precrash",
                "Uh oh...",
                "Hrmm...",
                "Oh shit...",
                "Uhm...",
                "*Rumble*",
                "What the.." + ".",
                "Oh god...");

        addRandom(
                "badores.crashium.crash",
                "Encrypting runtime devastation...",
                "Initializing pickaxe control...",
                "Shutting down Minecart Motor...",
                "Setting up Texture paintcans...",
                "Acquiring world dominance...",
                "Downloading corruption...",
                "Enderman on the loose!",
                "Calculating PI...",
                "Black hole too big!",
                "Hiding Herobrine...",
                "Swapping out Flux Capacitor...");

        addRandom(
                "badores.misleadium.baseMessage",
                "There is some %s at %sx, %sy, %sz!",
                "Go to %2$sx, %3$sy, %4$sz and you will find %1$s!",
                "I heard there's %s at %sx, %sy, %sz!",
                "Shh... I'll have you know I hid %s at %sx, %sy, %sz...",
                "There is %s at %sx, %sy, %sz!",
                "Please don't smelt me! Get the %s at %sx, %sy, %sz instead!",
                "How about you go to %2$sx, %3$sy, %4$sz? There is %1$s there!!");

        add("badores.website.fail", "Oops, failed to open a website!");

        add("badores.doesntevenexistium.name", "Doesntevenexistium");

        add("advancements.badores.badores.root.title", "Oh no...");
        add("advancements.badores.badores.root.description", "Mine a bad ore");
        add("advancements.badores.badores.find_barely_generite.title", "Finally found it!");
        add("advancements.badores.badores.find_barely_generite.description", "Find the famed Barely Generite");
        add("advancements.badores.badores.craft_barely_generite_block.title", "Spent too much time");
        add(
                "advancements.badores.badores.craft_barely_generite_block.description",
                "Find enough Barely Generite to make a full block");
        add("advancements.badores.badores.deal_iwontfite_damage.title", "Patience or Luck?");
        add("advancements.badores.badores.deal_iwontfite_damage.description", "Deal damage while holding Iwontfite");
        add("advancements.badores.badores.kill_nosleeptonite.title", "Conquer your fears");
        add("advancements.badores.badores.kill_nosleeptonite.description", "'Kill' a Nosleeptonite");
        add("advancements.badores.badores.mine_all_ores.title", "Gotta mine 'em all");
        add("advancements.badores.badores.mine_all_ores.description", "Collect all Bad Ores!");
        add("advancements.badores.badores.mine_killium.title", "Pfew.");
        add("advancements.badores.badores.mine_killium.description", "Mine Killium ore without dying");
        add("advancements.badores.badores.obtain_marmite_bread.title", "Hmmmmmm!");
        add("advancements.badores.badores.obtain_marmite_bread.description", "Make a piece of marmite bread");
        add("advancements.badores.badores.mine_zombieunite.title", "Zombie party");
        add("advancements.badores.badores.mine_zombieunite.description", "Mine Zombieunite");
        add("advancements.badores.badores.obtain_fleesonite_ingot.title", "Heartless Beast");
        add("advancements.badores.badores.obtain_fleesonite_ingot.description", "Smelt a poor, poor Fleesonsite");
        add("advancements.badores.badores.obtain_shiftium_ore.title", "Tricky...");
        add("advancements.badores.badores.obtain_shiftium_ore.description", "Acquire a Shiftium Block");

        addDescription(
                "polite",
                "First discovered in the west pole, this ore was labeled 'Polite'. It seems to live " + "a blithesome"
                        + " life underground, often found huddled together in groups. Still, it seems to enjoy " +
                        "being brought into new forms, and has no problem being worked into armor or tools.");
        addDescription(
                "crashium",
                "This ore has ascended to a higher form of existence. Our trivial matters concern " + "it not, and " +
                        "it" + " has dedicated itself to solving the problems of anti-determinism in the abstract " + "varidimensional multiverse. Good luck simulating that in Minecraft.");
        addDescription(
                "misleadium",
                "This mineral has widely been reported to give false information to the miner to" + " get spared " +
                        "from" + " harm. The name is misleading, though. While you think the information to be wrong," +
                        " " + "the" + " item does exist - until you look.");
        addDescription(
                "wannafite",
                "This ore seems to be reluctant to be hit in the head with a pickaxe, thrown into" + " the hot fires "
                        + "of a furnace, and then repeatedly being hammered down into the shape of a tool. It " +
                        "has " + "learned to fight back.");
        addDescription(
                "breakium",
                "Theoretically, Breakium is just a normal ore that is found below the surface. " + "However, past " + "mining experiments have revealed it to be cursed - All pickaxes get a broken shaft, a " + "crack in the metal, or just straight-up disappear when mining. Handle with care.");
        addDescription(
                "stonium",
                "What are you looking this up for. It's just stone. What, are you expecting every " + "rock to look " + "exactly the same?");
        addDescription(
                "crappium",
                "Nobody has ever found a good use for this ore. It's too common to be a commodity," + " too fragile " + "to make for tools or armor, and just too damn ugly to be purely decorational. You'll be" + " lucky if you don't have to pay to get rid of it.");
        addDescription(
                "enderite",
                "Over the years, some of the Endermen's activity has started to take its tolls " + "over some iron " + "ores, corrupting them with space anormalities. If you suddenly find yourself looking " + "at" + " a giant surface rapidly gaining speed towards your face, they are why.");
        addDescription(
                "lite",
                "Ohhh, pretty shiny light! Critics say the glow is caused by violent radiation, but " + "seriously, " + "you can't even see that stuff. Jackpot, free light source!");
        addDescription(
                "amadeum",
                "With plate tectonics come interesting side effects, sometimes. This almost " + "crystal-like " +
                        "mineral" + " strangely interacts with... Oh wait, you don't actually care about the " +
                        "mechanics, do " + "you? ... This ore makes funny sounds, and can be worked into decent armor" +
                        " and tools.");
        addDescription(
                "barely_generite",
                "Is this a real ore? Or is it just phantastic? Whatever the case, it was " + "only found once before,"
                        + " and is so exceedingly rare that you will probably never be able to make use " + "of it. " +
                        "If " + "you do, please tell us about its properties so we can expand this book!");
        addDescription(
                "unobtainium",
                "No methods applied to obtain this ore ever worked. Thus, not much is known - " + "but it would make "
                        + "for a goddamn amazing armor, if finally acquired!");
        addDescription(
                "ghostium",
                "You can see it, but you can't grab it. Thus, it was labeled 'ghostium' for " + "obvious reasons - " + "along with similar things like the 'ghostrays' from our sun, or the " + "'blue-ghost-stuff' " + "that you drink all day.");
        addDescription(
                "killium",
                "Mining this ore is so amazing, you can guarantee a heart attack afterwards. Just " + "try it " +
                        "yourself - I assure you you will never do anything as great afterwards!");
        addDescription(
                "uselessium",
                "Believe it or not, but this ore is even more useless than crappium. It " + "literally does nothing. "
                        + "Maybe it will go away if you concentrate on ignoring it long enough.");
        addDescription(
                "balancium",
                "To make up for long days spent mining 10000 stones, this balances out your " + "stress and " +
                        "frustration. It contains many shinies. What more do you need to know?");
        addDescription(
                "explodeitmite",
                "Main food source of creepers, this ore contains more explosive power than " + "the famed nuclear " + "weapons. Why does it not obliterate everything, you ask? Well, before the main " + "explosive peak is reached, the material is turned to ashes by its own explosion. Science.");
        addDescription(
                "movium",
                "You can never quite mine it... Just when you are about done, it just appears to " + "have moved. " + "Maybe you have just mined the wrong spot?");
        addDescription(
                "marmite",
                "All-time favourite bread spread. Contrary to popular belief, it's not organic " + "matter, but " +
                        "actually mined from stone. Try smelting it into its workable form, and enjoy tasty " +
                        "goodness!");
        addDescription(
                "shiftium",
                "Everything passes just right through it. You can't even remove it from its " + "containing stone, " + "much less use it. The only effective way of mining it is with pure energy - " + "explosions!");
        addDescription(
                "smite",
                "Smite is said to be Thor's favourite ore. It makes for absolutely awful armor, but " + "maybe you " + "can make some good tools out of it.");
        addDescription(
                "wantarite",
                "Somehow contains a pig. Don't ask me how, I'm just making these stories up. This" + " is honestly " + "asking too much of me.");
        addDescription(
                "meteorite",
                "Mining this ore somehow attracts cosmic forces, and makes it rain stone or " + "netherrack. As long "
                        + "as you mine it underground, you should be fine, but remember to warn others " + "above " +
                        "when " + "you do mine it!");
        addDescription(
                "streetscum",
                "Excellent pickpocket, especially considering it shouldn't even be able to move." + " Try to keep " + "your distance from it.");
        addDescription(
                "fleesonsite",
                "This ore literally gets up and runs from you when it senses you nearby, " + "crashing head-first " + "into walls, jumping into lava, or running in circles. Consider your own sanity " + "before " + "slaughtering these cute buggers.");
        addDescription(
                "nopium",
                "Seems to just slip out of your hands, or even your pockets, all the time. Once, a " + "man dedicated"
                        + " his whole life to making himself its master, and crafted a whole armor from them. The " + "result: The armor would somehow just fall off. Maybe you should take this lesson and mine " + "more " + "worthwhile ores.");
        addDescription(
                "idlikeabite",
                "Mining this ore is so extremely tedious that you tire from the work and " + "afterwards wonder " +
                        "where" + " the nearest delicious chicken leg was again.");
        addDescription(
                "zombieunite",
                "This ore somehow seems to sense the amount of zombies, and becomes less hard " + "to mine with a " + "higher amount. It is therefore not easy to obtain. Protip: Open public facebook event" + " " + "'Zombie party - everyone invited!'");
        addDescription(
                "paintitwhite",
                "In all actuality, this 'ore' is really just a skeleton ground into little " + "bits by time and " +
                        "tectonics. Maybe it could be used for coloring textiles, or as a fertilizer for " + "your " + "orchis garden.");
        addDescription(
                "iwontfite",
                "Just carrying this ore makes your limbs all wobbly, and your muscles seem to " + "lose all strength" +
                        "." + " This, dear Steve, is the incredible power of pacifists.");
        addDescription(
                "lookslikediamondium",
                "For whatever reason, there are diamond-impostors around in the ground," + " too, probably to get " +
                        "the" + " respect of other ores. Many a miner have found themselves at home, holding" + " up " +
                        "their " + "prize from a long day of work, to discover it was actually just " +
                        "Lookslikediamondium.");
        addDescription(
                "tauntum",
                "Some of the noises this mineral makes sound freakishly similar to animals or " + "monsters. Maybe " + "you should mine it just to get rid of distractions.");
        addDescription(
                "kakkarite",
                "This ore is so incredibly powerfully compressed, you have no idea. Like, wow. " + "Mine it, and you "
                        + "will be showered with kakkarite ingots, maybe about 9000 of them.");
        addDescription(
                "pandaemonium",
                "Mining this ore rips a hole in the inter-dimensional veils, and lets the " + "nether out into the " + "overworld. 2/10, not recommended.");
        addDescription(
                "nosleeptonite",
                "The ore of your nightmares. The dark king of them all, and otherwise just " + "the incarnation of " + "your deepest fears. AVOID");
        addDescription(
                "doesntevenexistium",
                "This is a common ore found deep underground, which serves default " + "practical purposes slightly " + "superior to iron.");
        addDescription(
                "appetite",
                "Dude, are you seriously contemplating eating this mineral, whole? It has " + "literally just been " + "burrowed in sand, for who knows how long... Just don't, man. Have some " + "self-respect.");
        addDescription(
                "website",
                "This ore has discovered a way to break the 4th wall and found a tunnel out of the " + "box. It " +
                        "slowly discoveres the infinite possibilities outside of Minecraft.");
    }

    public void addRandom(String key, String... values) {
        add(key + ".count", String.valueOf(values.length));
        for (int i = 0; i < values.length; i++) {
            add(key + "." + i, values[i]);
        }
    }

    public void addDescription(String name, String description) {
        add("badores." + name + ".description", description);
    }
}
