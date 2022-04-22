# LingQWordsExport
Will export all your words from **LingQ** https://lingq.com
to a tab-separated UTF-8 **text** file, **Html**, and **Anki**.

THE APP IS UNDER ACTIVE DEVELOPMENT RIGHT NOW, EVERYTHING'S PROVIDED AS IS...

Here is an application that can download word definitions from LingQ learning portal.
The app can try to restore capitalization, which is important for German language:<br>
    – essen (to eat) – das Essen (the food/the eating)) <br>
In addition, pictures and audio are automatically downloaded.
With this data the application can make html learning pages and anki files.

1) Make sure you have **Java 11+ installed**. Use "Java Downloads.url" to get the distributive.
<br><br>
2) **Edit config.ini**: <br>
- APIKey= &emsp;  'your LingQ key, use "LingQ API Key.url" to get it <br>
- lang_code=de &emsp;'language code, run app to see all available codes <br>
- download_mode=local &emsp;'download and process "all" words, or only "update", or only from "local" file <br>
- display_languages=yes &emsp;'displays a user's languages list if "yes" <br>
- transform_words=yes &emsp;'searches for a word in an example sentence if "yes" <br>
- download_mp3s=yes &emsp;'save google-translate mp3s for each word <br>
- download_pics=yes &emsp;'get images from google or bing for each word <br>
- generate_html=yes &emsp;'create html file with words, audio and pictures <br>
- generate_anki=yes &emsp;'make a file that can be imported to Anki <br>
- max_pages=2 &emsp;'maximum pages that the app will read (25 words per page) <br>
- download_pics_from=google &emsp;'where to get pics from - google or bing <br>
- connectionString=https://www.lingq.com/api/ &emsp;'LingQ API URL <br>
- file_name=lingq_words.txt &emsp;'a file to save the words to <br>
- file_name_html=lingq_words.html &emsp;'to html folder <br>
- file_name_anki=lingq_words.tab &emsp;'to anki folder <br>
<br>
3) **Run LingQWordsExport_RUN.bat** (displays Java version and runs the app).

A resulting file can be imported in Excel - use UTF-8 charset, tab-separated. 
To test it all, I recommend running the app once with max_pages set to 2. 
After you've made sure you got the words saved, change it to 20000 or so.

4) Review **html files with words**. To avoid creating humongous html files, when there are too many words, the app splits the list to several html files.<br>
Html file displays <br>
* a word
* translation
* fragment of an example sentence
* sound play button
* a picture

5) **Import data to Anki**. First import LingQ words.apkg to create initial note and cards (optional). <br>
Then copy image and audio files to Anki's collection.media folder. <br>
You can use a script copy_img&mpr_to_anki_media.bat, set the precise path to media folder <br>
that is located in the username folder. <br>
Next step is to import a file with words to Anki. The file contains following fields: <br>
   * Word
   * Translation
   * Example
   * Audio
   * Image
<br><br>
6) **Enjoy**.

I tested this all on a Windows 10 machine. There should be no particular problems running it on other platforms, please let me know how it goes for you. <br>
If the file with words (file_name) is found, it will be loaded, if not - the words will be downloaded from LingQ.
Run several instances of the app to download audio and image file faster (I trided 4 in parallel).

I'm open for suggestions and help. I wish y'all huge success with language learning!

Sergey Svistunov
