# LingQWordsExport
Will export all your words from LingQ.

Requires Java 11+ to work. Use "Java Downloads.url" to get fresh Java distributive.

Edit config.ini: <br>
connectionString=https://www.lingq.com/api/ 'api URL <br>
APIKey=xxx  'your LingQ key, use "LingQ API Key.url" to get it  <br>
file_name=lingq_words.txt 'the app saves your words, tab separated  <br>
lang_code=de 'language code, run app once to read available languages  <br>
max_pages=20000 'maximum pages (25 words per page) <br>
transform_words=yes 'LingQ saves words in low case, so for German the app searches the word in the example sentence  <br>

A resulting file can be imported in Excel. Use UTF-8 charset. First I recommend to run the app with max_pages set to 2. After making sure you got the words saved, change it to 20000 or so.

I wish you all the success with language learning!

Sergey Svistunov
