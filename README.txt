-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: Once you have a command window up in the folder that the downloaded material was placed, type:

Windows: "gradlew setupDecompWorkspace"
Linux/Mac OS: "./gradlew setupDecompWorkspace"

Step 3: After all that finished, you're left with a choice.
For eclipse, run "gradlew eclipse" (./gradlew eclipse if you are on Mac/Linux)

If you preffer to use IntelliJ, steps are a little different.
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Once it's finished you must close IntelliJ and run the following command:

"gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)

Step 4: The final step is to open Eclipse and switch your workspace to /eclipse/ (if you use IDEA, it should automatically start on your project)

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not effect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.

Tip:
If you do not care about seeing Minecraft's source code you can replace "setupDecompWorkspace" with one of the following:
"setupDevWorkspace": Will patch, deobfusicated, and gather required assets to run minecraft, but will not generated human readable source code.
"setupCIWorkspace": Same as Dev but will not download any assets. This is useful in build servers as it is the fastest because it does the least work.

Tip:
When using Decomp workspace, the Minecraft source code is NOT added to your workspace in a editable way. Minecraft is treated like a normal Library. Sources are there for documentation and research purposes and usually can be accessed under the 'referenced libraries' section of your IDE.

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html


-------------------------------------------

插件的源安装信息

-------------------------------------------

此代码遵循Minecraft Forge的安装方法。它将适用

香草MCP源代码的一些小补丁，让您和它可以访问

对于一些数据和功能，你需要建立一个成功的mod。



还需注意的是，补丁是根据“未命名”MCP源代码（又名

srgnames）-这意味着您将无法直接针对

正常代码。



源程序包安装信息：



独立源安装

==============================



步骤1：打开命令行，浏览到提取zip文件的文件夹。



步骤2：在放置下载材料的文件夹中打开命令窗口后，键入：



Windows:“gradlew setupDecompWorkspace”

Linux/Mac操作系统：“./gradlew setupDecompWorkspace”



第三步：完成所有这些之后，你还有一个选择。

对于eclipse，运行“gradlew-eclipse”（如果您在Mac/Linux上，则运行./gradlew-eclipse.）



如果您喜欢使用IntelliJ，步骤会有所不同。

1.打开IDEA，导入项目。

2.选择build.gradle文件并将其导入。

3.完成后，必须关闭IntelliJ并运行以下命令：



“gradlew-genIntellijRun”（./gradlew-genIntellijRuns，如果您在Mac/Linux上）



第4步：最后一步是打开Eclipse并将您的工作区切换到/Eclipse/（如果您使用IDEA，它应该会自动在您的项目中启动）



如果在任何时候IDE中缺少库，或者遇到问题，可以运行“gradlew-refresh dependencies”来刷新本地缓存。“gradlew-clean”重置所有内容｛这不会影响您的代码｝，然后重新启动进程。



如果它仍然不起作用，

有关渐变环境的更多信息，请参阅EsperNet上的#ForgeGradle。



提示：

如果你不想看到Minecraft的源代码，你可以用以下代码之一替换“setupDecompWorkspace”：

“setupDevWorkspace”：将修补、去商业化和收集运行minecraft所需的资产，但不会生成人类可读的源代码。

“setupCIWorkspace”：与Dev相同，但不会下载任何资产。这在构建服务器时很有用，因为它是最快的，因为它做的工作最少。



提示：

当使用解压缩工作空间时，Minecraft源代码不会以可编辑的方式添加到您的工作空间中。《我的世界》被视为一个普通的图书馆。源代码用于文档和研究目的，通常可以在IDE的“参考库”部分访问。



锻造源安装

=========================

MinecraftForge附带此代码并将其作为锻造的一部分进行安装

安装过程中，不需要您采取进一步的操作。



LexManos的安装视频

=======================

https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be



有关更多详细信息更新，请参阅Forge论坛：

http://www.minecraftforge.net/forum/index.php/topic，14048.0.html