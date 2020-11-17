package io.github.rudeyeti.bteintegration;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SyncBuilders {
    public static void sync(Guild guild) {
        try {
            for (int i = 1; i < BTEIntegration.lastPage + 1; i++) {
                Document membersPage = Jsoup.connect(BTEIntegration.buildTeamMembers + "?page=" + i).userAgent("BTEIntegration").get();
                Elements td = membersPage.select("td");

                for (int a = 1; a < td.size(); a += 3) {
                    String username = td.get(a).text();
                    Member member = guild.getMemberByTag(username);
                    Role role = guild.getRoleById(BTEIntegration.roleID);

                    if (member != null && role != null && !member.getRoles().contains(role)) {
                        guild.addRoleToMember(member, role).queue();
                        if (BTEIntegration.configuration.getBoolean("log-role-changes")) {
                            BTEIntegration.logger.info("The user " + username + " was promoted to: " + role.getName());
                        }
                    }
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
