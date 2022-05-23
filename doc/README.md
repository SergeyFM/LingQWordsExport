## LingQWordsExport documentation page

LingQWordsExport is thought out to transfer words from LingQ to text files and Anki. It is open source application with MIT licensing. 

### Hi! 
I'm Sergey Svistunov. I've made this app to make it easier for myself to learn words that I accumulate while reading on LingQ. As usual, a quick idea have grown into a project. I use LingQWordsExport daily and hope you'll find it useful too.

### How does it work?
The app connects to the [LingQ API](https://www.lingq.com/apidocs/index.html) and downloads the word definitions. Then it uses Google-translate services to get the sound of the word spoken out loud. And finally, the app fetches images for the words from Google or/and Bing. <br>
There are certain peculiarities in how LingQ deals with words and sentences. One such peculiarity that annoys me the most is removal of letter's case information - all the words and sentences are saved in lowercase format, and it doesn't go well with German language:<br>
_– essen (to eat) – das Essen (the food/the eating))_ <br> To fix this issue the app searches for the word in a given fragment and tries to restore the original word.

### What is the standard use case?
As you've finished a lesson on LingQ, you run the app and it downloads all the new words together with images and audio. You import the file with new words into Anki (adding to an existing deck or creating a new one) and copy all the media files (with one click on a batch-file). <br>
Now you can learn the words and sentences in Anki.

### How to run the application?

1. Download the app [LingQWordsExport.zip](https://github.com/SergeyFM/LingQWordsExport/raw/master/distr/LingQWordsExport.zip), unzip it. For the app to launch correctly you need an application jar file, config.ini, and empty folders: anki, html, mp3 and pic.
2. Make sure you have Java 11+ installed ([download page](https://www.oracle.com/java/technologies/downloads/)).
3. Edit the `config.ini` file. Look below to see further details on configuration.
4. Run `LingQWordsExport_RUN.bat`. It is a command line interface application, the batch file will first display a JAVA version, then a jar file will be launched.
5. As all the activities are finished you should have output files in the corresponding folders. <br> /anki/ lingq_words.tab - you import this file into Anki <br> /html/ lingq_words.html - one or more files for web-browsers <br> /mp3/ *.mp3 - audio files for each word <br> /pic/ *.jpeg - image files, one for each word
6. Now you can import data to Anki. The latest *.tab file contains words. `copy_img&mpr_to_anki_media.bat` copies all the media files to the Anki's `collection.media` folder (please set this folder in the file before use). Import `LingQ words.apkg` to create initial note and cards (optional).

This is my [my daily routine](MyDailyRoutine.md). First I read on LingQ and then study new words in Anki.

### How to configure the app?

Installation and configuration process is described [here](Installation.md) with screenshots. <br>
Briefly speaking, all settings are saved in the `config.ini` file in the root application folder. <br>
It contains the following: <br>
- **APIKey**= &emsp;  'your LingQ key, use "LingQ API Key.url" to get it <br>
- **lang_code**=de &emsp;'language code, run app to see all available codes <br>
- download_mode=update &emsp;'download and process "all" words, or only "update", or only from "local" file <br>
- display_languages=yes &emsp;'displays a user's languages list if "yes" <br>
- **transform_words**=yes &emsp;'searches for a word in an example sentence if "yes" <br>
- download_mp3s=yes &emsp;'save google-translate mp3s for each word <br>
- download_pics=yes &emsp;'get images from google or bing for each word <br>
- generate_html=yes &emsp;'create html file with words, audio and pictures <br>
- generate_anki=yes &emsp;'make a file that can be imported to Anki <br>
- **max_pages**=20000 &emsp;'maximum pages that the app will read (25 words per page) <br>
- download_pics_from=google &emsp;'where to get pics from - google or bing <br>
- connectionString=https://www.lingq.com/api/ &emsp;'LingQ API URL <br>
- file_name=lingq_words.txt &emsp;'a file to save the words to <br>
- file_name_html=lingq_words.html &emsp;'to html folder <br>
- file_name_anki=lingq_words.tab &emsp;'to anki folder <br>

For starters, you should add your LingQ API Key (get it [here](https://www.lingq.com/en/accounts/apikey/)) and set your language code. Set transform_words to no if you don't want the app to change the words in any way. Also, if you don't want to download old words from LingQ, you can set a limit with max_pages - one page is 25 words (recent words go first).<br>

<br>
Sergey Svistunov.
