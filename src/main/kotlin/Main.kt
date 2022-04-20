val path = System.getProperty("user.dir")
val settings = Config(path)

fun main(args : Array<String>) {
    
    println("\n   -=< LingQ Words Export >=-   \n")
    
    if(args.isNotEmpty()) println("No args accepted, use config.ini")
    
    // --------------------------------- Read settings -----------------------------------------------------------------
    println("Working Directory: $path")
    println("Settings from config.ini: \n" +
            settings.ini.map{
                    (k,v)-> if(k=="APIKey") " > $k = ***" + v.takeLast(10) else " > $k = $v"
            }.joinToString("\n"))
    val cnn_attr = Connection(settings["connectionString"], settings["APIKey"])
    val file = settings["file_name"]
    val pathfile = "$path\\$file"
    val lang_code = settings["lang_code"]
    
    // --------------------------------- Test connection to LingQ API --------------------------------------------------
    print("Test connection ... ")
    if(isConnectionOK(cnn_attr)) println("OK")
    else {println("SOMETHING WRONG!")}

    // --------------------------------- Display a list of languages ---------------------------------------------------
    if(settings["display_languages"]=="yes") {
        print("LingQ languages and known words: ")
        val languages = getListOfLanguages(cnn_attr)
        if(languages.size==0) println(" NONE")
        else println("\n" + languages.map{" > $it"}.joinToString("\n"))
    }
    
    // --------------------------------- Download or read words definitions --------------------------------------------
    // download_mode= 'download and process "all" words, or only "update", or only from "local" file
    val download_mode = settings["download_mode"]
    // read a file of already downloaded words
    val file_already_exists = fileExists(pathfile)
    val file_words: List<Word> = if(download_mode!="all" && file_already_exists) {
        print("Read $pathfile...")
        loadWordsFromFile(pathfile)
    }  else listOf<Word>()
    if(file_words.isNotEmpty()) println(" ${file_words.size} words") else println("")
    
    // download new or all words
    val my_words: List<Word> = if(download_mode=="local") {
        print("Process words from the file: ")
        file_words // if "local" we process all the words from a file
    } else if(download_mode=="update") {
        print("Download only new words from LingQ, pages: ")
        getListOfWords(cnn_attr,lang_code,settings["max_pages"].toInt(),file_words)
    } else {
        print("Download words from LingQ, pages: ")
        getListOfWords(cnn_attr,lang_code,settings["max_pages"].toInt())
    }
    println("${my_words.size} words")
    
   
   // --------------------------------- Fix word's capitalization ------------------------------------------------------
    val (transf_words: List<Word>, transf_result: Int) = if(settings["transform_words"]=="yes") {
        print("Get words from the example sentences...")
        transformWords(my_words)
    } else Pair(my_words,0)
    
    
    // --------------------------------- Save words to a *.txt file ----------------------------------------------------
    if(transf_words.size>0 && transf_words.size!=file_words.size || transf_result>0) {
        val all_words = if(download_mode=="local") transf_words.distinctWords() else (file_words + transf_words).distinctWords()
        var one_more_time = false
        do {
            print("\nSave " + all_words.size + " words  to $pathfile... ")
            val saved = saveFile(all_words,pathfile)
            println(saved)
            if(saved=="Nothing") break
            if(saved!="OK") {
                print("\n saving wasn't OK, try one more time? y/n ___ ")
                val reply: String = readLine()?.uppercase() ?: "N"
                println("REPLY: [$reply]")
                one_more_time = if(reply=="Y") true else false
            }
        } while(saved!="OK" && one_more_time==true)
    }
    
    
    // --------------------------------- Download mp3 files ------------------------------------------------------------
    if(settings["download_mp3s"]=="yes") {
        val MAX_ATTEMPTS = 3
        print("\nDownload mp3... ")
        var current_letter = ""
        var saved_files_counter = 0
        var existing_files_counter = 0
        var something_wrong = 0
        for(word in transf_words.sortedBy{it.word}) {
            val first_letter = word.word.take(1)
            if(first_letter!=current_letter) {
                current_letter=first_letter
                print("\n$first_letter (" + transf_words.count{it.word.first()==first_letter.first()} + "):")
            }
            for(attempt in (1..MAX_ATTEMPTS)) {
                if(attempt>1) print(attempt)
                val mp3filename: String = wordToFilename(word.word,".mp3")
                val saved = downloadGooleAudio("$path\\mp3\\$mp3filename",lang_code,word.word,false)
                when(saved) {
                    "OK" -> {saved_files_counter++; break}
                    "EXISTS" -> {existing_files_counter++; break}
                    else -> if(attempt==MAX_ATTEMPTS) something_wrong++
                }
            }
        }
        println("\n $saved_files_counter new files, $existing_files_counter already exist, $something_wrong errors")
    }
    
    // --------------------------------- Download pictures -------------------------------------------------------------
    if(settings["download_pics"]=="yes") {
        val MAX_ATTEMPTS = 3
        val engine = settings["download_pics_from"]
        print("\nDownload pictures ($engine)... ")
        var current_letter = ""
        var saved_files_counter = 0
        var existing_files_counter = 0
        var something_wrong = 0
        for(word in transf_words.sortedBy{it.word}) {
            val first_letter = word.word.take(1)
            if(first_letter!=current_letter) {
                current_letter=first_letter
                print("\n$first_letter (" + transf_words.count{it.word.first()==first_letter.first()} + "):")
            }
            for(attempt in (1..MAX_ATTEMPTS)) {
                if(attempt>1) print(attempt)
                val pic_filename: String = wordToFilename(word.word,".jpeg")
                val saved = downloadPicture("$path\\pic\\$pic_filename",lang_code,word.word,word.translation+" "+word.fragment,false,engine)
                when(saved) {
                    "OK" -> {saved_files_counter++; break}
                    "EXISTS" -> {existing_files_counter++; break}
                    else -> if(attempt==MAX_ATTEMPTS) something_wrong++
                }
            }
        }
        println("\n $saved_files_counter new files, $existing_files_counter already exist, $something_wrong errors")
    }
    
    //--------------------------------- Generate HTML file -------------------------------------------------------------
    if(settings["generate_html"]=="yes") {
        val SPLIT_LIMIT = 10000
        val html_filename = settings["file_name_html"]
        val words_num = transf_words.size
        print("\nSave $words_num words to the HTML file $html_filename ... ")
        if(transf_words.size>SPLIT_LIMIT) {
            println(" \n there are too many words, html file will be split:")
            val first_letters = transf_words.mapNotNull{it.word.firstOrNull()?.uppercaseChar()}.distinct().sorted().filter{it.isLetter()}
            first_letters.forEach {letter->
                val html_pathfile = "$path\\html\\$letter" + "_" + html_filename
                val select = transf_words.filter{it.word.firstOrNull()?.uppercaseChar()==letter}
                print("$letter:${select.size}")
                val save_html_res = saveToHTMLfile(select,html_pathfile)
                print(":$save_html_res ")
            }
            println("")
        } else {
            val html_pathfile = "$path\\html\\" + html_filename
            val save_html_res = saveToHTMLfile(transf_words,html_pathfile)
            println("$save_html_res")
        }
    }
    
    //--------------------------------- Generate import file for Anki --------------------------------------------------
    if(settings["generate_anki"]=="yes") {
        var one_more_time = false
        val words_num = transf_words.size
        val anki_file = "$path\\anki\\" + settings["file_name_anki"]
        do {
            print("\nSave $words_num words to the Anki-file $pathfile... ")
            val saved = saveToAnkiFile(transf_words,anki_file)
            println(saved)
            if(saved=="Nothing") break
            if(saved!="OK") {
                print("\n saving wasn't OK, try one more time? y/n ___ ")
                val reply: String = readLine()?.uppercase() ?: "N"
                println("REPLY: [$reply]")
                one_more_time = if(reply=="Y") true else false
            }
        } while(saved!="OK" && one_more_time==true)
    }
    
}