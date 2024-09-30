# Minux

Have you ever thought that chat in Minecraft is too boring?
Make it more functional by connecting any of the shells to it!

### Preview

![mod_preview](https://github.com/user-attachments/assets/d959b56c-c870-43a6-83ca-161de490eae2)

### Configuration

#### Recommendations

By default, the shell command is set to `bash`, offering a native experience on Linux and utilizing WSL on Windows.
If [WSL](https://learn.microsoft.com/windows/wsl/install) is not available in your Windows environment, you can switch
the shell command to `cmd`.

If you want to change the shell start command, you can use one of the following methods

#### Via Commands

| Command                       | Description                                         |
|-------------------------------|-----------------------------------------------------|
| `/minux shell get running`    | Display the command that started the running shell  |
| `/minux shell get configured` | Display the command configured to start a new shell |
| `/minux shell set <command>`  | Set a new command to start the next shell           |
| `/minux restart`              | 	Restart the current shell                          |

#### Via GUI

Available only with installed [Mod Menu](https://modrinth.com/mod/modmenu)
