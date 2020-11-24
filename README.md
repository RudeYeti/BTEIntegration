# BTEIntegration
**Author:** Rude Yeti, Incorporated  
**Latest Version:** 1.2

A simple plugin which allows synchronization from the BuildTheEarth Website to Discord and Minecraft Servers. Through the technique of web scraping, the plugin gathers data from the BuildTheEarth Website and uses it for modifying the roles of different members so everything matches properly. The syncing only triggers when a change in the number of builders is detected, so everything isn't updated too frequently, only when it's necessary.

### Dependencies
 - **Required:** Mohist - https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.16.4/lastSuccessfulBuild/  
 - **Required:** DiscordSRV - https://www.spigotmc.org/resources/discordsrv.18494/  
 - **Required:** Vault - https://www.spigotmc.org/resources/vault.34315/  
 - **Required:** Permissions Plugin, **Recommended:** LuckPerms - https://www.spigotmc.org/resources/luckperms.28140/

Due to Forge's lack of plugin support, Mohist will be necessary for the ability to have any Spigot or Bukkit plugins. Moving on from the server software, DiscordSRV is used to simplify bot setup. You can also use its other features, including having a Discord Channel which displays chat from the Minecraft Server. Besides that, you also need Vault, and any permissions plugin though LuckPerms is recommended. Those two will allow for promoting different users to permission groups, so the builders get their roles on the server as well. 

### Installation
 - Confirming that you are using Mohist, drop all of the downloaded dependencies from above including BTEIntegration from the [releases page](https://github.com/rudeyeti/BTEIntegration/releases) into the ```/plugins/``` folder.
 - Start the server to let the configuration files of the various plugins generate.
 - Ensure that your Discord Bot is invited to your Build Team's Discord Server, so it's able to promote members.
 - Make sure that the permissions plugin has a group created exclusively for builders, then it's able to give players their roles.
 - Open up DiscordSRV's main settings located in ```/plugins/DiscordSRV/config.yml``` and replace the ```BotToken``` value with your Discord Bot's token, this bot will be accessed from both DiscordSRV and the BTEIntegration plugin.
 - Follow the instructions in the below Configuration section so BTEIntegration is ready to start synchronization.
 - Lastly, restart the whole server so everything newly installed can refresh.

### Configuration
Here is the default configuration of the BTEIntegration plugin, most values need to be modified before using it on a server. If some options are set to the default the plugin will disable itself, so make sure you set it up properly.
```
# BTEIntegration plugin created by Rude Yeti, Incorporated.
# A simple plugin which allows synchronization from the BuildTheEarth Website to Discord and Minecraft Servers.
# This depends on DiscordSRV, Vault, and a permissions plugin. They are a requirement for this plugin to work.

# General Configuration

# Build Team Members webpage on the BuildTheEarth Website, required to fetch the member list.
# Default value: "https://buildtheearth.net/buildteams/#/members"
build-team-members: "https://buildtheearth.net/buildteams/#/members"
```
This value should be appropriately changed to match the respective page for your specific Build Team, just remember that the page must have the number and not the vanity link. As long as it ends up on the member page, it will work properly.
```
# Discord Role ID, this should be the exact id of the builder role in the Discord Server.
# Default value: "##################"
discord-role-id: "##################"
```
Whichever is the role that you want your builders promoted to should go here, but you need Developer Mode enabled in the settings to locate the role's ID. This ID can be obtained from both the Role Settings page or just by right-clicking on a user's role list.
```
# Minecraft Role Name, the precise role name of the Minecraft Server's builder role.
# Default value: "builder"
minecraft-role-name: "builder"
```
The permissions plugin should have groups configured, so this should be the name of whatever you called it for your builders.
```
# Whether role changes should just affect recently promoted/demoted users, or if it should update everyone else.
# Sometimes this can be useful for cleaning up roles given by accident in the past, so this is enabled by default.
# Default value: true
global-role-changes: true
```
This typically only happens on the first go-through, but this plugin can remove the role of people that aren't actually registered on the website. Sometimes this can be useful, but other times there are special cases when you only want the recently accepted person to have their roles modified. If that is the case then change it to false, otherwise it should be okay kept as true. Neither value is better or preferred, but each has their own specific uses.
```
# If you want each time a builder is promoted to be logged to the Minecraft Server Console, enable this.
# Default value: false
log-role-changes: false
```
Logging role changes to the console should be fairly self-explanatory, but all this does is record when people have their roles changed by this plugin. Usually it can clog up the console if people are promoted very often, but other times it can be very helpful to see if someone was given the correct role.

### Commands and Permissions
**/bteintegration:**
 - Usage: `/bteintegration <info | reload | stats>`
 - Description: Displays information about the three subcommands.
 
 **/bteintegration info**:
 - Usage: `/bteintegration info`
 - Description: Shows details about the author and the version of this plugin.
 
  **/bteintegration reload**:
 - Usage: `/bteintegration reload`
 - Permission: `bteintegration.reload`
 - Description: Updates any values that were modified in the configuration.
 
  **/bteintegration stats**:
 - Usage: `/bteintegration stats`
 - Permission: `bteintegration.stats`
 - Description: Lists different information regarding the members.

### Build Instructions
Double check that [Git](https://git-scm.com/downloads) and [Apache Maven](https://maven.apache.org/download.cgi) are installed on your computer, they are a requirement to download and build files from GitHub. Make sure to select the proper versions for your Operating System. After that, open up a Terminal or the Command Line and clone this repository:  
```
git clone https://github.com/rudeyeti/BTEIntegration.git
```  

Once that has finished, navigate to the newly created folder and launch Apache Maven. You can do this by running the following command which builds the jar file:  
```
mvn clean package
```

Once a success message appears, that means everything has been compiled properly. You can find the jar with this file name:
```
BTEIntegration-1.2.jar
```