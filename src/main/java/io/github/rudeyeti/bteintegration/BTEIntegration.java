package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.rudeyeti.bteintegration.commands.BTEIntegrationReload;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class BTEIntegration extends JavaPlugin {

    public static Plugin plugin;
    public static Configuration configuration;
    public static Logger logger;
    public static String buildTeamMembers;
    public static Guild guild;
    public static String roleID;
    public static Role role;
    public static String group;
    public static String initialBuilders;
    public static int lastPage;
    private final DiscordSRVListener discordSRVListener = new DiscordSRVListener();

    public static Permission getPermissions() {
        try {
            Class.forName("net.milkbowl.vault.permission.Permission");
            RegisteredServiceProvider<Permission> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            return provider.getProvider();
        } catch (ClassNotFoundException error) {
            error.printStackTrace();
            return null;
        }
    }

    public static void validateConfiguration() {
        if (buildTeamMembers.equals("https://buildtheearth.net/buildteams/0/members")) {
            logger.warning("The build-team-members value in the configuration must be modified from https://buildtheearth.net/buildteams/0/members.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        } else if (roleID.equals("000000000000000000")) {
            logger.warning("The discord-role-id value in the configuration must be modified from 000000000000000000.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        this.saveDefaultConfig();
        this.getCommand("bteintegrationreload").setExecutor(new BTEIntegrationReload());
        getPermissions();

        plugin = getPlugin(this.getClass());
        configuration = this.getConfig();
        logger = this.getLogger();
        roleID = configuration.getString("discord-role-id");

        DiscordSRV.api.subscribe(discordSRVListener);
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(discordSRVListener);
    }
}
