# Readme

This is the source code for the game 'Symbol Jump' which can be found on [Google Play](https://play.google.com/store/apps/details?id=com.symbolplay.tria.android). 'Tria' is just an internal code name I used for this project.

## How to download the source and build the game

Please note that I am writing this from memory, so there might be some uncertainties or errors. Regardless, you might find this helpful.

Prerequisites:

  * Install Java. I think JRE is enough, but I was using 32-bit JDK. You can find it [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
  * Install [Eclipse](https://eclipse.org/). I was using 32-bit Eclipse 4.4.0, but any later (and probably some previous) versions should be fine. I'm also guessing you could use 64-bit versions, but you should probably also have a 64-bit version of Java then. You could probably also use some other IDE, such as [Android Studio](https://developer.android.com/sdk/index.html), but I didn't use it when making this game so I cannot give you any instructions on this.
  * Install [Standalone Android SDK Tools](https://developer.android.com/sdk/index.html#Other).
  * Install ADT Plugin for Eclipse (https://dl-ssl.google.com/android/eclipse/).
  * After installing ADT Plugin, go to `Window->Android SDK Manager`, find the SDK path if necessary and install Android stuff necessary for development. From 'Tools' folder I select: 'Android SDK Tools', 'Android SDK Platform-tools' and latest version of 'Android SDK Build-tools'. From 'Extras' folder I select: 'Android Support Library' and 'Google USB Driver'. And finally I select the folder with the latest Android version (at the the time I am writing this, the latest version is 'Android 5.1.1 (API 22)').
  * Install Gradle for Eclipse (http://dist.springsource.com/release/TOOLS/gradle). You will need this to download LibGdx library.
  * Install Git (in the unlikely case you don't have it already). You can find a Windows version of git [here](http://git-scm.com/). You can also find some documentation on git [here](http://git-scm.com/documentation).

Downloading the source code:

  * Clone this repository to some folder on your disk (`git clone git@github.com:mrzli/tria.git`).
  * Clone [libgdxgamelibrary](https://github.com/mrzli/libgdxgamelibrary) repository to the same folder where you cloned 'tria' project (`git clone git@github.com:mrzli/libgdxgamelibrary.git`).