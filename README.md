# BTEIntegration
**Author:** Rude Yeti, Incorporated  
**Latest Version:** 1.0.1

A simple plugin which allows synchronization from the BuildTheEarth Website to Discord and Minecraft Servers. Through the technique of web scraping, it gathers data from the website and uses it for integrating everything together in an automated style. It periodically scans the specified Build Team website searching for an update in the number of builders; once that is found it resyncs the roles for everyone to designate the user as an official Builder in the Discord Server. More upcoming features are planned, including a better implementation of Minecraft Server Permissions, more efficient HTTP Requests for reduced memory usage, and an overhauled promotion and demotion system. Aside from that, it functions very well and runs smoothly.

### Dependencies
 - **Required:** Mohist - https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.16.4/lastSuccessfulBuild/  
 - **Required:** DiscordSRV - https://www.spigotmc.org/resources/discordsrv.18494/  
 - **Required:** Vault - https://www.spigotmc.org/resources/vault.34315/  
 - **Required:** Permissions Plugin
 - **Optional:** LuckPerms - https://www.spigotmc.org/resources/luckperms.28140/

This is obviously a plugin and not a mod, so you will need to install Mohist instead of Forge. Moving on from the server software, DiscordSRV is used to provide the Discord Bot libraries, while reducing the file size of the overall plugin. You can use DiscordSRV to help with other types of syncing as well, including a Discord Channel displaying Minecraft Chat. Besides that, you also need Vault, and any permissions plugin though LuckPerms is recommended. Those two will allow for promoting different users to permission groups, so the builders get their roles on the server as well. 

### Installation
 - Confirming that you are using Mohist, drop all of the downloaded dependencies from above including BTEIntegration from the [releases page](https://github.com/rudeyeti/BTEIntegration/releases) into the ```/plugins/``` folder.
 - Start the server to let the configuration files of the various plugins generate.
 - Ensure that your Discord Bot is invited to your Build Team's Discord Server, so it's able to promote members.
 - Make sure that the permissions plugin has a group created exclusively for builders, then it's able to give players their roles.
 - Open up DiscordSRV's main settings located in ```/plugins/DiscordSRV/config.yml``` and replace the ```BotToken``` value with your Discord Bot's token, this bot will be accessed from both DiscordSRV and the BTEIntegration plugin.
 - Follow the instructions in the below Configuration section so BTEIntegration is ready to start synchronization.
 - Lastly, restart the whole server so everything newly installed can refresh.

### Configuration
Here is the default configuration of the BTEIntegration plugin, most values need to be modified before using it on a server. If the options are set to the default the plugin will disable itself, so make sure you set it up properly.
```
# BTEIntegration plugin created by Rude Yeti, Incorporated.
# A simple plugin which allows synchronization from the BuildTheEarth Website to Discord and Minecraft Servers.
# This depends on DiscordSRV, Vault, and a permissions plugin. They are a requirement for this plugin to work.

# General Configuration

# Build Team Members webpage on the BuildTheEarth Website, required to fetch the member list.
# Default value: "https://buildtheearth.net/buildteams/0/members"
build-team-members: "https://buildtheearth.net/buildteams/0/members"

# Discord Role ID, this should be the exact id of the builder role in the Discord Server.
# Default value: "000000000000000000"
discord-role-id: "000000000000000000"

# Minecraft Role Name, the precise role name of the Minecraft Server's builder role.
# Default value: "builder"
minecraft-role-name: "builder"

# If you want each time a builder is promoted to be logged to the Minecraft Server Console, enable this.
# Default value: false
log-role-changes: false
```
Replace the build-team-members section with your Build Team's own website, vanity links will not work, it must be from the members page with the number in the link. You will also need to find the ID of the Builder Role in your Discord Server, that requires you to be in Developer Mode to find it. Right clicking any role usually works, if not you can view it from the Roles Page in the Discord Server's Settings. To have players properly given the Builder Group, change the minecraft-role-name part to whatever you have registered in your permissions plugin. Lastly, if you want each time a user is promoted or demoted to be recorded in the Server Console, then turn the last option to true. Otherwise there will be no logging of any kind, which means you will not know when users get their roles.

### Commands/Permissions
**/bteintegrationreload:**
 - Description: Reloads the BTEIntegration plugin.
 - Usage: /bteintegrationreload
 - Permission: bteintegration.reload

### Build Instructions
Double check that [Git](https://git-scm.com/downloads) and [Apache Maven](https://maven.apache.org/download.cgi) are installed on your computer, they are a requirement to download and build files from GitHub. Make sure to select the proper versions for your Operating System. After that, open up a Terminal or the Command Line and clone this repository:  
```
git clone https://github.com/rudeyeti/BTEIntegration.git
```  

Once that has finished, navigate to the newly created folder and launch Apache Maven. You can do this by running the following command which builds the jar file:  
```
mvn package
```

Once a success message appears, that means everything has been compiled properly. You can find the jar in the relative folder with this file path, if this does not appear than there must have been an error with the building process:  
```
/target/bteintegration-1.0.1.jar
```