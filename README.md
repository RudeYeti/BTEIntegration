# BTEIntegration
A simple plugin which allows synchronization from the BuildTheEarth Website to Discord and Minecraft Servers. Through the technique of web scraping, it gathers data from the website and uses it for integrating everything together in an automated style. It periodically scans the specified Build Team website searching for an update in the number of builders; once that is found it resyncs the roles for everyone to designate the user as an official Builder in the Discord Server. More upcoming features are planned, including a better implementation of Minecraft Server Permissions, more efficient HTTP Requests for reduced memory usage, and an overhauled promotion and demotion system. Aside from that, it functions very well and can be found in the [releases page](https://github.com/rudeyeti/BTEIntegration/releases).

### Configuration
Here is the default configuration of the BTEIntegration plugin, most values need to be modified before using it on a server. If the options are set to the default the plugin will disable itself, so make sure you set it up properly.
```
# BTEIntegration plugin created by Rude Yeti, Incorporated.
# Allows for easy syncing between the BuildTheEarth Website, a Discord Server, and a Minecraft Server.
# This depends on DiscordSRV, it is a requirement for this plugin to work.

# General Configuration

# Build Team Members webpage on the BuildTheEarth Website, required to fetch the member list.
# Default value: "https://buildtheearth.net/buildteams/0/members"
build-team-members: "https://buildtheearth.net/buildteams/0/members"

# Discord Role ID, this should be the exact id of the builder role in the Discord Server.
# Default value: "000000000000000000"
discord-role-id: "000000000000000000"

# If you want each time a builder is promoted to be logged to the Minecraft Server Console, enable this.
# Default value: false
log-role-changes: false
```
Replace the build-team-members section with your Build Team's own website, vanity links will not work, it must be from the members page with the number in the link. You will also need to find the ID of the Builder Role in your Discord Server, that requires you to be in Developer Mode to find it. Right clicking any role usually works, if not you can view it from the Roles Page in the Discord Server's Settings. Lastly, if you want each time a user is promoted to be recorded in the Server Console, then turn the last option to true. Otherwise there will be no logging of any kind, which means you will not know when users get their roles.

### Build Instructions
Double check that [Git](https://git-scm.com/downloads) and [Apache Maven](https://maven.apache.org/download.cgi) are installed on your computer, they are a requirement to download files from GitHub. Make sure to select the proper versions for your Operating System. After that, open up a Terminal or the Command Line and clone this repository:  
```
git clone https://github.com/rudeyeti/BTEIntegration.git
```  

Once that has finished, navigate to the newly created folder and launch Apache Maven. You can do this by running the following command which builds the jar file:  
```
mvn package
```

Once a success message appears, that means everything has been compiled properly. You can find the jar in the relative folder with this file path, if this does not appear than there must have been an error with the building process:  
```
/target/bteintegration-1.0.0.jar
```

### Dependencies
Remember that this plugin relies on [DiscordSRV](https://github.com/DiscordSRV/DiscordSRV). It is used to provide the Discord Bot libraries, while reducing the file size of the overall plugin. You can use DiscordSRV to help with other types of syncing as well, including a Discord Channel displaying Minecraft Chat.