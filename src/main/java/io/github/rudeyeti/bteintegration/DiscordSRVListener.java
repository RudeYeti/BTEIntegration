package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscordSRVListener {
    @Subscribe
    public void discordReadyEvent(DiscordReadyEvent event) {
        Document membersFirstPage;
        String builders;
        while (true) {
            try {
                membersFirstPage = Jsoup.connect(BTEIntegration.buildTeamMembers + "?page=1").userAgent("BTEIntegration").get();
                builders = membersFirstPage.select("small").text();

                if (BTEIntegration.initialBuilders.equals(builders)) {
                    continue;
                }

                BTEIntegration.initialBuilders = builders;
                BTEIntegration.membersFirstPage = membersFirstPage;
                BTEIntegration.lastPage = Integer.parseInt(membersFirstPage.select("div.pagination").select("a").last().text());

                for (String guild : Objects.requireNonNull(DiscordUtil.getJda().getGuildCache().applyStream(guild -> guild.map(Guild::getId).collect(Collectors.toList())))) {
                    SyncBuilders.sync(Objects.requireNonNull(DiscordUtil.getJda().getGuildById(guild)));
                }
            } catch (IOException error) {
                error.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException error) {
                error.printStackTrace();
            }
        }
    }
}