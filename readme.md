# Live MOTD
A quick and easy way to display a live, customizable MOTD on your server.

### Source Code
[github.com/FlauschigesAlex](https://github.com/FlauschigesAlex/live-motd)


### Supported platforms & versions
- [Paper](https://papermc.io/software/paper/) Versions: 1.21.10 - 26.1.2
- [PurpurMC](https://purpurmc.org/) Versions: 1.21.10 - 26.1.2
- [Velocity](https://velocitypowered.com/) Versions: 3.4.0 - 3.5.0

Although live-motd may work on other platforms or versions, I do not guarantee for their stability or functionality.


### Configuration:
```json
{
  "richMOTD": "<rainbow>MOTD with MiniMessage support!</rainbow> <newline><blue><bold>Welcome to my minecraft server!"
} 
```

![motd preview](https://github.com/FlauschigesAlex/live-motd/blob/branding/preview/motd-preview-mc.png?raw=true)<br>
``richMOTD`` A 'rich string' representing the message of the day.<br>
Based on [MiniMessage formatting](https://docs.papermc.io/adventure/minimessage/format/). Supports multiline components using `\n` or `<newline>`.

## Commands
``/live-motd`` Displays the current MOTD in the chat.<br>
``/live-motd <MOTD>`` Changes the MOTD to the provided string.<br>
``/live-motd --reload-config`` Reloads the configuration file.

Aliases:
 - ``/motd``
 - ``/message-of-the-day``

## Permissions
``live-motd.command.use`` Allows the holder to use the ``/live-motd`` command.