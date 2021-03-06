# LingQWordsExport
Exports all your words from **[LingQ](https://lingq.com)** 
to a tab-separated UTF-8 **text** file, **Html**, and **Anki**.

You can download the app here: [LingQWordsExport.zip](https://github.com/SergeyFM/LingQWordsExport/raw/master/distr/LingQWordsExport.zip) <br>
Please read the [documentation](doc/README.md) page. <br>
Installation and configuration of the app is described [here](doc/Installation.md). <br>
Or you can jump right to [my daily routine](doc/MyDailyRoutine.md) page showing all the steps I make getting the words from LingQ to Anki. <br>

## What's this
The app connects to the [LingQ API](https://www.lingq.com/apidocs/index.html) and downloads words and sentences. Then it uses Google-translate service to get the sound of the word spoken out loud. And finally, the app fetches images from Google or Bing. <br>
There are certain peculiarities in how LingQ deals with words and sentences. One such thing is removal of letter's case information - all the words and sentences are saved in lowercase format, and it doesn't go well with German language:<br>
_– essen (to eat) – das Essen (the food/the eating))_ <br> 
To fix this issue the app searches for the word in a given fragment and tries to restore the original word.
<br> <br>
You will have your words ready for review, and you can easily print them out: <br>
![](doc/a22.png) <br> <br>
And Anki will help learning them: <br>
![](doc/a33.png)

## Selected features
* gets the words from LingQ
* fixes capitalization issues (for German)
* downloads pictures from google and bing
* and google-translate audio for each word or sentence
* gets all the words or only recently added words
* exports to txt, html and anki


## GitHub repository
There are following folders: 
 * app - the application ready for run
 * distr - archived app folder [LingQWordsExport.zip](https://github.com/SergeyFM/LingQWordsExport/raw/master/distr/LingQWordsExport.zip)
 * doc - documentation folder
 * src/main/kotlin - source code

## Quick start
If you want to try the application:

1. Read [documentation](doc/README.md) in the  [documentation folder](https://github.com/SergeyFM/LingQWordsExport/tree/master/doc)

2. Download the app: [LingQWordsExport.zip](https://github.com/SergeyFM/LingQWordsExport/raw/master/distr/LingQWordsExport.zip), unzip it

3. Make sure you have Java 11+ installed ([download page](https://www.oracle.com/java/technologies/downloads/))

4. Edit config.ini - add your key and set the language

5. Run LingQWordsExport_RUN.bat 

6. Review html files with words

7. Import data to Anki

8. Enjoy

Once set up, import takes a few minutes.

I tested the application on a Windows 10 machine. There should be no difficulties running it on other platforms, please let me know how it goes for you. <br>
You can run several instances of the app to speed up audio and images download (I tried 4 in parallel).

I'm open for suggestions and help. Any feedback is appreciated. <br>
I wish y'all enormous success with language learning!

Sergey Svistunov
