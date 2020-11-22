package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.requests.GatewayIntent;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.rudeyeti.bteintegration.commands.BTEIntegrationReload;
import io.github.rudeyeti.bteintegration.listeners.DiscordSRVListener;
import io.github.rudeyeti.bteintegration.listeners.EventListener;
import io.github.rudeyeti.bteintegration.listeners.JDAListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class BTEIntegration extends JavaPlugin {

    public static String prefix = "[BTEIntegration] ";
    public static Plugin plugin;
    public static Configuration configuration;
    public static Logger logger;
    public static List<Member> initialBuildTeamMembersList;
    public static Member lastRoleChange;
    public static Guild guild;
    public static Role role;
    public static int lastPage;

    public static String buildTeamMembers;
    public static String discordRoleId;
    public static String minecraftRoleName;
    public static boolean globalRoleChanges;
    public static boolean logRoleChanges;
    
    public static Permission getPermissions() {
        RegisteredServiceProvider<Permission> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        return provider.getProvider();
    }

    public static void updateConfiguration() {
        buildTeamMembers = configuration.getString("build-team-members");
        discordRoleId = configuration.getString("discord-role-id");
        minecraftRoleName = configuration.getString("minecraft-role-name");
        globalRoleChanges = configuration.getBoolean("global-role-changes");
        logRoleChanges = configuration.getBoolean("log-role-changes");
    }
    
    public static boolean validateConfiguration() {
        updateConfiguration();

        if (buildTeamMembers.equals("https://buildtheearth.net/buildteams/#/members")) {
            logger.warning("The build-team-members value in the configuration must be modified from https://buildtheearth.net/buildteams/#/members.");
            return false;
        } else if (discordRoleId.equals("##################")) {
            logger.warning("The discord-role-id value in the configuration must be modified from ##################.");
            return false;
        } else if (!Pattern.compile("^https?://(www\\.)?buildtheearth.net/buildteams/\\d+/members$").matcher(buildTeamMembers).matches()) {
            logger.warning("The build-team-members value in the configuration does not match the format https://buildtheearth.net/buildteams/#/members.");
            return false;
        } else if (!(configuration.get("global-role-changes") instanceof Boolean)) {
            logger.warning("The global-role-changes value in the configuration must be either true or false.");
            return false;
        } else if (!(configuration.get("log-role-changes") instanceof Boolean)) {
            logger.warning("The log-role-changes value in the configuration must be either true or false.");
            return false;
        }

        return true;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        this.saveDefaultConfig();
        this.getCommand("bteintegrationreload").setExecutor(new BTEIntegrationReload());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getPermissions();
        plugin = getPlugin(this.getClass());
        logger = this.getLogger();
        configuration = this.getConfig();
        updateConfiguration();

        DiscordSRV.api.requireIntent(GatewayIntent.GUILD_MEMBERS);
        DiscordSRV.api.subscribe(new DiscordSRVListener());
    }

    @Override
    public void onDisable() {
        DiscordUtil.getJda().removeEventListener(new JDAListener());
        DiscordSRV.api.unsubscribe(new DiscordSRVListener());
    }
}
