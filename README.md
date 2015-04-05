# Readme

This is the source code for the game 'Symbol Jump' which can be found on [Google Play](https://play.google.com/store/apps/details?id=com.symbolplay.tria.android). 'Tria' is just an internal code name I used for this project.

## How to download the source and build the game

Please note that I am writing this from memory, so there might be some uncertainties or errors. Regardless, you might find this helpful. Downloading/installation section uses a `.bat` file so it will only run on Windows. On other operating systems you will need to do the steps in the batch file manually.

Prerequisites:

  * Install [Java Development Kit 7 (JDK) or later version](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
  * Install [Eclipse](https://eclipse.org/). I was using 32-bit Eclipse 4.4.0, but any later (and probably some previous) versions should be fine. I'm also guessing you could use 64-bit versions, but you should probably also have a 64-bit version of Java then. You could probably also use some other IDE, such as [Android Studio](https://developer.android.com/sdk/index.html), but I didn't use it when making this game so I cannot give you any instructions on this.
  * Install [Standalone Android SDK Tools](https://developer.android.com/sdk/index.html#Other).
  * Install ADT Plugin for Eclipse (https://dl-ssl.google.com/android/eclipse/).
  * After installing ADT Plugin, go to `Window->Android SDK Manager`, find the SDK path if necessary and install Android stuff necessary for development. From 'Tools' folder I select: 'Android SDK Tools', 'Android SDK Platform-tools' and latest version of 'Android SDK Build-tools'. From 'Extras' folder I select: 'Android Support Library' and 'Google USB Driver'. And finally I select the folder with the latest Android version (at the the time I am writing this, the latest version is 'Android 5.1.1 (API 22)').
  * Install USB drivers for your mobile phone if necessary. For example, I was using Samsung Galaxy S3 and was able to download the drivers from [here](http://www.samsung.com/us/support/owners/product/SCH-I535MBBVZW).
  * Install Gradle for Eclipse (http://dist.springsource.com/release/TOOLS/gradle). You will need this to download LibGdx library.
  * Install Git (in the unlikely case you don't have it already). You can find a Windows version of git [here](http://git-scm.com/). You can also find some documentation on git [here](http://git-scm.com/documentation).

Downloading/installing:

  * Clone this repository to some folder on your disk (`git clone git@github.com:mrzli/tria.git`).
  * Clone [libgdxgamelibrary](https://github.com/mrzli/libgdxgamelibrary) repository to the same folder where you cloned 'tria' project (`git clone git@github.com:mrzli/libgdxgamelibrary.git`).
  * Create a new Eclipse workspace. I usually put the workspace in the same folder where the repositories were cloned.
  * Import game projects. In Eclipse select `File->Import...`, then `Gradle->Gradle Project`, then browse to the folder of 'tria' repository and press `Build Model` (in case of problems read on).
  * If it whines that it can't find SDK location for Android, create a file called `local.properties` (or edit existing) in 'tria' folder and add the SDK path line like this one (use your SDK location): `sdk.dir=C:/Tools/Android/android-sdk`. Select all projects for import and click OK.
  * If you different version of Android API than what is used in this project, do the following changes in android project. In `AndroidManifest.xml` change `android:targetSdkVersion` to your version, and do the same in file `project.properties` to `target=android-##` line.

## Credits (for the work of other people)

The font used for this game is:

  * Open Sans font - [www.opensans.com](http://www.opensans.com)

All (original) sounds used in the game are taken from [freesound.org](freesound.org). Please note that some of these were edited and changed in some way for usage in the game. Here is a list of sounds and the links to where the originals can be found:

  * Jump - [Porphyr](http://freesound.org/people/Porphyr/sounds/187567/)
  * Jump Boost - [plingativator](http://freesound.org/people/plingativator/sounds/188869/)
  * Coin - [fins](http://freesound.org/people/fins/sounds/146723/)
  * Item - [cameronmusic](http://freesound.org/people/cameronmusic/sounds/138410/)
  * Ouch - [under7dude](http://freesound.org/people/under7dude/sounds/163441/)
  * Saw - [Benboncan](http://freesound.org/people/Benboncan/sounds/60178/)
  * Spikes - [qubodup](http://freesound.org/people/qubodup/sounds/166535/)
  * Fall - [silversatyr](http://freesound.org/people/silversatyr/sounds/113366/)
  * Rocket - [genelythgow](http://freesound.org/people/genelythgow/sounds/203264/)
  * AG - [peridactyloptrix](http://freesound.org/people/peridactyloptrix/sounds/213384/)

## License

[MIT License](License.txt). Pretty much do what you want with this code and assets. Attribution would be nice but is not a requirement. Please note that font and sounds are work of other people (I only edited some sounds to suit my needs) and have their own licenses (see previous section).
