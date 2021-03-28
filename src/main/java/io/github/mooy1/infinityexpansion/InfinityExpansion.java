package io.github.mooy1.infinityexpansion;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import io.github.mooy1.infinityexpansion.commands.GiveRecipe;
import io.github.mooy1.infinityexpansion.commands.PrintItem;
import io.github.mooy1.infinityexpansion.commands.SetData;
import io.github.mooy1.infinityexpansion.implementation.Setup;
import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.mooy1.infinitylib.bstats.bukkit.Metrics;
import io.github.mooy1.infinitylib.commands.AbstractCommand;
import lombok.Getter;

public final class InfinityExpansion extends AbstractAddon {
    
    private static InfinityExpansion instance;
    
    public static InfinityExpansion inst() {
        return instance;
    }
    
    @Getter
    private double difficulty = 1;
    
    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        
        loadDifficulty();
        
        getMetrics().addCustomChart(new Metrics.SimplePie("difficulty", () -> String.valueOf(this.difficulty)));
        
        boolean lXInstalled = getServer().getPluginManager().getPlugin("LiteXpansion") != null;
        
        if (lXInstalled) {
            runSync(() -> log(Level.WARNING,
                    "###################################################################################",
                    "LiteXpansion has done some nerfs on a few of this addon's items,",
                    "specifically solar panels, aswell as some of Slimefun's items.",
                    "If you don't want these nerfs, you will need to remove LiteXpansion.",
                    "If you want to keep LiteXpansion, make a suggestion to them to add config options.",
                    "Any complaints as a result of this should be directed to LiteXpansion.",
                    "###################################################################################"
            ));
        }
        
        getMetrics().addCustomChart(new Metrics.SimplePie("litexpansion_installed", () -> String.valueOf(lXInstalled)));
        
        Setup.setup(this);
    }

    @Override
    protected int getMetricsID() {
        return 8991;
    }

    @Override
    protected String getGithubPath() {
        return "Mooy1/InfinityExpansion/master";
    }

    @Override
    protected List<AbstractCommand> getSubCommands() {
        return Arrays.asList(new GiveRecipe(), new SetData(), new PrintItem());        
    }

    public void loadDifficulty() {
        double val = getConfig().getDouble("balance-options.difficulty");
        // round to .1 .2 .3 or 1 2 3 etc
        if (val >= .1 && val < 1) {
            this.difficulty = ((int) (val * 10)) / 10D;
        } else if (val >= 1 && val <= 10) {
            this.difficulty = (int) val;
        } else {
            this.difficulty = 1;
            getConfig().set("balance-options.difficulty", 1);
            log(Level.WARNING, "Difficulty value was out of bounds, resetting to default!");
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

	public double getDifficulty() {
		// TODO Auto-generated method stub
		return this.difficulty;
	}
    
}
