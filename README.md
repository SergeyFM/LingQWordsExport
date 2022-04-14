# LingQWordsExport
Will export all your words from LingQ https://lingq.com
to a tab-separated UTF-8 text file.

THE APP IS UNDER ACTIVE DEVELOPMENT RIGHT NOW, EVERYTHING'S PROVIDED AS IS...

I bring to your attention a program for importing words from LingQ. 
Unfortunately, LingQ keeps words case-insensitive. 
Therefore, I added a case correction function to the program - 
the program looks for a word in an example sentence and takes it from there.
The words are then saved to a text file, delimited by tabs.

1) Make sure you have Java 11+ installed. Use "Java Downloads.url" to get the distributive.

2) Edit config.ini: <br>
APIKey=xxx  // your LingQ key, use "LingQ API Key.url" to get it  <br>
file_name=lingq_words.txt // a file to save words to <br>
words_src=lingq // lingq: download files form LingQ, file: from the file <br>
lang_code=de // language code, run app to see all available codes  <br>
max_pages=20000 // maximum pages that the app will read (25 words per page) <br>
transform_words=yes // "yes" for case correction  <br>
download_mp3s=yes // save google-translate mp3s for each word

4) Run LingQWordsExport_RUN.bat (displays Java version and runs the app).

A resulting file can be imported in Excel - use UTF-8 charset, tab-separated. 
To test it all, I recommend running the app once with max_pages set to 2. 
After you've made sure you got the words saved, change it to 20000 or so.

5) Use word-list from html folder to review exported data.
6) Import data in Anki. First import LingQ words.apkg to create initial note and cards. <br>
Then import a file that contains following fields: <br>
   * Word
   * Translation
   * Example
   * Audio
   * Image

I wish y'all huge success with language learning!

Sergey Svistunov
