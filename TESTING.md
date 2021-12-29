#### Requirements
Running Ion requires Redis, and MongoDB. While Redis is easy and should just work after it is installed and running, MongoDB is a little more complicated.

For MongoDB you need to ensure that MongoDB is started with the options `--replSet rs0 --bind_ip_all`. How to set this will vary from system to system, so please look for the appropriate documentation, if you can't find it then feel free to ask but please ACTUALLY SPEND MORE THAN A FEW MINUTES LOOKING.

Once MongoDB is started, you need to open a mongo shell in your terminal or your "command prompt" for the Windows users, and run `rs.initiate();db.createUser({user:"test",pwd:"test",roles:[{role:"readWrite",db:"test"}]});`. Feel free to change the username, password, and database name however you will need to remember what you changed it to later.

#### Building the plugin
Build the plugin using the provided run configuration in IntelliJ or alternatively run the gradle task "`reobfJar`" either in your IDE or from the terminal:

Linux `./gradlew reobfJar`

Windows `gradlew.bat reobfJar`

MacOS X *why are you using a mac?*

The jar file of Ion will be `./build/Ion.jar`

#### Setting up the test server.
Grab yourself a Paper 1.18.1 jar file from (https://papermc.io/downloads) and go through the normal setup process for a server.

Then it's time for dependencies... oh boy.

Lets start with the easy ones.

- Dynmap from (https://github.com/webbukkit/dynmap/releases)
- WorldEdit from (https://dev.bukkit.org/projects/worldedit/files) **OR** FastAsyncWorldEdit from (https://intellectualsites.github.io/download/fawe.html)
- DiscordSRV from (https://github.com/DiscordSRV/DiscordSRV/releases)
- Luckperms from (https://luckperms.net/download)
- Citizens from (https://ci.citizensnpcs.co/job/Citizens2/)

Now for the more annoying one.
GriefDefender will require you to manually build it from source as while technically a paid plugin, it is under MIT license, so we can do what we please with it.
Download the source code from (https://github.com/bloodmc/GriefDefender) and build it using an IDE or though the terminal.

Linux `./gradlew shadowJar`

Windows `gradlew.bat shadowJar`

#### Configuration
If earlier you set custom database login details you will need to configure them in the `config.yml` file created by the plugin after first launch.

#### Running the test server
Just start the server lol
