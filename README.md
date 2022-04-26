# LingQWordsExport
Will export all your words from **[LingQ](https://lingq.com)** 
to a tab-separated UTF-8 **text** file, **Html**, and **Anki**.

Here you can find a [documentation](doc/README.md) page. Or jump right to my [my daily routine](doc/MyDailyRoutine.md) with screenshots. <br>


The app connects to the [LingQ API](https://www.lingq.com/apidocs/index.html) to download words and sentences definitions. Then it uses [Google-translate](https://translate.google.com/) service to get the sound of the word spoken out loud. And finally, the app fetches images for the words from Google or Bing. <br>
There are certain peculiarities in how LingQ deals with words and sentences. One such annoying peculiarity is removal of letter's case information - all the words and sentences are saved in lowercase format, and it doesn't go well with German language:<br>
_– essen (to eat) – das Essen (the food/the eating))_ <br> 
To fix this issue the app searches for the word in a given fragment and tries to restore the original word.

<br> If you want to try the application:

1. Read the [documentation folder](https://github.com/SergeyFM/LingQWordsExport/tree/master/doc)

2. Download the app: [LingQWordsExport.zip](https://github.com/SergeyFM/LingQWordsExport/raw/master/distr/LingQWordsExport.zip), unzip it

3. Make sure you have Java 11+ installed ([download page](https://www.oracle.com/java/technologies/downloads/))

4. Edit config.ini - add your key and set the language

5. Run LingQWordsExport_RUN.bat 

6. Review html files with words

7. Import data to Anki

8. Enjoy

Once set up, import takes a few minutes.

I tested the application on a Windows 10 machine. There should be no particular problems running it on other platforms, please let me know how it goes for you. <br>
You can run several instances of the app to download audio and images faster (I tried 4 in parallel).

I'm open for suggestions and help. Any feedback is appreciated. <br>
I wish y'all enormous success with language learning!

Sergey Svistunov
