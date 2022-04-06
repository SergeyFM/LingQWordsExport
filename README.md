# LingQWordsExport
Will export all your words from LingQ https://lingq.com
to a tab-separated UTF-8 text file.

I bring to your attention a program for importing words from LingQ. 
Unfortunately, LingQ keeps words case-insensitive. 
Therefore, I added a case correction function to the program - 
the program looks for a word in an example sentence and takes it from there.
The words are then saved to a text file, delimited by tabs.

1) Make sure you have Java 11+ installed. Use "Java Downloads.url" to get the distributive.

2) Edit config.ini: <br>
APIKey=xxx  // your LingQ key, use "LingQ API Key.url" to get it  <br>
file_name=lingq_words.txt // the app saves your words, tab separated  <br>
lang_code=de // language code, run app once to read available languages  <br>
max_pages=20000 // maximum pages (25 words per page) <br>
transform_words=yes // "yes" for case correction  <br>

3) Run LingQWordsExport_RUN.bat (displays Java version and runs the app).

A resulting file can be imported in Excel - use UTF-8 charset, tab-separated. 
To test it all, I recommend running the app once with max_pages set to 2. 
After you've made sure you got the words saved, change it to 20000 or so.

I wish y'all success with language learning!

Sergey Svistunov
