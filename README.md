# LingQWordsExport
Will export all your words from LingQ https://lingq.com
to a tab-separated UTF-8 text file.

THE APP IS UNDER ACTIVE DEVELOPMENT RIGHT NOW, EVERYTHING'S PROVIDED AS IS...

Here is an application that can download word definitions from LingQ learning portal.
The app can try to restore lost case information, which is important for German language:<br>
    – essen (to eat) – das Essen (the food/the eating)) <br>
In addition, pictures and audio is automatically downloaded.
With this data the application can make html learning pages and anki files.

1) Make sure you have Java 11+ installed. Use "Java Downloads.url" to get the distributive.

2) Edit config.ini: <br>
display_languages=yes // if "yes" the list of user's languages is displayed
words_src=lingq // lingq: download files form LingQ, file: from the file <br>
transform_words=yes // "yes" for case correction  <br>
save_results_to_file=yes // should the results be saved to a file
download_mp3s=yes // save google-translate mp3s for each word
download_pics=yes // get images from google or bing
download_pics_from=google // google or bing, google works much better
generate_html=yes // will create html file, for each letter, if too many words
generate_anki=yes // creates a file that can be imported to Anki

APIKey=xxx  // your LingQ key, use "LingQ API Key.url" to get it  <br>
connectionString=https://www.lingq.com/api/
file_name=lingq_words.txt // a file to save the words to <br>
file_name_html=lingq_words.html // in html folder
file_name_anki=lingq_words.tab // in anki folder
lang_code=de // language code, run app to see all available codes  <br>
max_pages=40000 // maximum pages that the app will read (25 words per page) <br>

4) Run LingQWordsExport_RUN.bat (displays Java version and runs the app).

A resulting file can be imported in Excel - use UTF-8 charset, tab-separated. 
To test it all, I recommend running the app once with max_pages set to 2. 
After you've made sure you got the words saved, change it to 20000 or so.

5) Use word-list from html folder to review exported data.

6) Import data to Anki. First import LingQ words.apkg to create initial note and cards (optional). <br>
Then copy image and audio files to Anki's collection.media folder. <br>
After that import a file with words. The file contains following fields: <br>
   * Word
   * Translation
   * Example
   * Audio
   * Image

I tested this all on a Windows 10 machine. There should be no particular problems running it on other platforms, please let me know how it goes for you.

I'm open for suggestions and help. I wish y'all huge success with language learning!

Sergey Svistunov
