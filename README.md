# Mr. Krabs Bot

Requirements:
Built in IntelliJ Ultimate, using gradle.
* Best to use Ultimate Version of IntelliJ, the community version does not support springboot applications without 3rd party extension.
* If community edition, have to find a 3rd party extension that supports springboot applications.


### Clone
Clone a copy of the repo:
```bash
git clone https://github.com/sw-code/mrkrabs-bot.git
```
Build after specifying springboot settings in IntelliJ.

To find out and edit the port being used, check applications.properties file.

### Emulator
Install the [Bot framework emulator](https://github.com/microsoft/BotFramework-Emulator/releases) and follow the instructions to connect.

**INFO**:The bot URL looks like this "http://localhost:3979/api/messages" Please remember to add "/api/messages" at the end of bot URL if you change the port number later. You can configure the port number as you please in the application.properties file.

