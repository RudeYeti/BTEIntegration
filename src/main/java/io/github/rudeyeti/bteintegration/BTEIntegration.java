package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.logging.Logger;

public final class BTEIntegration extends JavaPlugin {

    public static Configuration configuration;
    public static Logger logger;
    public static String buildTeamMembers;
    public static String roleID;
    public static Document membersFirstPage;
    public static String initialBuilders;
    public static int lastPage;
    private final DiscordSRVListener discordSRVListener = new DiscordSRVListener();

    @Override
    public void onEnable() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            this.saveDefaultConfig();

            configuration = this.getConfig();
            logger = this.getLogger();
            buildTeamMembers = configuration.getString("build-team-members");
            roleID = configuration.getString("discord-role-id");

            if (buildTeamMembers.equals("https://buildtheearth.net/buildteams/0/members")) {
                logger.warning("The build-team-members value in the configuration must be modified.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            } else if (roleID.equals("000000000000000000")) {
                logger.warning("The discord-role-id value in the configuration must be modified.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            membersFirstPage = Jsoup.connect(buildTeamMembers + "?page=1").userAgent("BTEIntegration").get();
            initialBuilders = membersFirstPage.select("small").text();
            lastPage = Integer.parseInt(membersFirstPage.select("div.pagination").select("a").last().text());
            DiscordSRV.api.subscribe(discordSRVListener);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(discordSRVListener);
    }
}
