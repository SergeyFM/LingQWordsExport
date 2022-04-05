# LingQWordsExport
Will export all your words from LingQ https://lingq.com
to a tab-separated UTF-8 text file.

1) Requires Java 11+ to work. Use "Java Downloads.url" to download and install a fresh Java distributive.

2) Edit config.ini: <br>
APIKey=xxx  'your LingQ key, use "LingQ API Key.url" to get it  <br>
file_name=lingq_words.txt 'the app saves your words, tab separated  <br>
lang_code=de 'language code, run app once to read available languages  <br>
max_pages=20000 'maximum pages (25 words per page) <br>
transform_words=yes 'LingQ saves words in low case, so if "yes" the app searches the word in the example sentence  <br>

3) Run LingQWordsExport_RUN.bat (displays Java version and runs the app) or LingQWordsExport.jar itself.

A resulting file can be imported in Excel - use UTF-8 charset, tab-separated. 
First I recommend to run the app with max_pages set to 2. 
After making sure you got the words saved, change it to 20000 or so.

I wish y'all success with language learning!

Sergey Svistunov
