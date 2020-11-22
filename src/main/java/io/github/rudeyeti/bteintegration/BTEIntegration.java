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

    public static Plugin plugin;
    public static Configuration configuration;
    public static Logger logger;
    public static String buildTeamMembers;
    public static List<Member> buildTeamMembersList;
    public static List<Member> initialBuildTeamMembersList;
    public static Guild guild;
    public static String roleID;
    public static Role role;
    public static String group;
    public static int lastPage;

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

    public static boolean validateConfiguration() {
        buildTeamMembers = configuration.getString("build-team-members");
        roleID = configuration.getString("discord-role-id");
        group = configuration.getString("minecraft-role-name");

        if (buildTeamMembers.equals("https://buildtheearth.net/buildteams/#/members")) {
            logger.warning("The build-team-members value in the configuration must be modified from https://buildtheearth.net/buildteams/#/members.");
            return false;
        } else if (roleID.equals("##################")) {
            logger.warning("The discord-role-id value in the configuration must be modified from ##################.");
            return false;
        } else if (!Pattern.compile("^https?://(www\\.)?buildtheearth.net/buildteams/\\d+/members$").matcher(buildTeamMembers).matches()) {
            logger.warning("The build-team-members value in the configuration does not match the format https://buildtheearth.net/buildteams/#/members.");
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

        roleID = configuration.getString("discord-role-id");
        buildTeamMembers = configuration.getString("build-team-members");
        group = configuration.getString("minecraft-role-name");

        DiscordSRV.api.requireIntent(GatewayIntent.GUILD_MEMBERS);
        DiscordSRV.api.subscribe(new DiscordSRVListener());
    }

    @Override
    public void onDisable() {
        DiscordUtil.getJda().removeEventListener(new JDAListener());
        DiscordSRV.api.unsubscribe(new DiscordSRVListener());
    }
}
