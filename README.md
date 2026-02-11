![Modrinth Downloads](https://img.shields.io/modrinth/dt/velocity-alert?label=Modrinth%20Downloads&logo=modrinth&color=brightgreen)
![GitHub Downloads](https://img.shields.io/github/downloads/Anwenden001/Velocity-Alert-Plugin/total?label=GitHub%20Downloads&logo=github&color=blue)
# Velocity Alert Plugin
## Overview
A simple Velocity plugin that alerts/announces players in your entire server network. You can use this feature to warn players about an upcoming server/proxy restart, or announce any other important message you want to mention.

## Features
* /alert (message)
* Permission node is `alert.command.permission` (recommended for staff only; you can setup the permissions in your permission plugin)
* Colored messages with HEX color support
* Config file where you can customise the alert message prefixes and colors.

### Supported Formats
A list of supported formats with links on how to use them.
- [Legacy](https://www.digminecraft.com/lists/color_list_pc.php)
- [JSON](https://minecraft.wiki/w/Raw_JSON_text_format)
- [XML (MiniMessages)](https://docs.advntr.dev/minimessage/format.html#minimessage-format)

### Format Examples
Here are some basic examples of those 3 different formats: (<span style="color:red">[ALERT] Warning!</span>)
- Legacy: `/alertlegacy &cWarning!`
- JSON: `/alertjson {"text":"Warning!","color":"red"}`
- XML (MiniMessage): `/alertxml <red>Warning!`

Or you can use `/alert` by changing the format in the config file. The defaults format is lagacy.

## Installation and Setup
1. Download the latest Alert plugin [here](https://github.com/Anwenden001/Velocity-Alert-Plugin/releases/tag/v1.8) from GitHub or from [Modrinth](https://modrinth.com/plugin/velocity-alert).
2. Place the plugin in the plugins folder of your proxy server.
3. Restart your proxy and that's it! The plugin should be loaded.

<hr>

If you have any questions or have found a problem, please report it to  [https://github.com/Anwenden001/Velocity-Alert-Plugin/issues](https://github.com/Anwenden001/Velocity-Alert-Plugin/issues)
