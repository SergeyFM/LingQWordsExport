fun main(args : Array<String>) {
    
    println("\n   -=< LingQ Words Export >=-   \n")
    
    if(args.isNotEmpty()) println("No args accepted, use config.ini")
    
    val path = System.getProperty("user.dir")
    println("Working Directory: $path")
    
    var one_more_time = false
    
    // --------------------------------- Read settings -----------------------------------------------------------------
    val settings = Config(path)
    println("Settings from config.ini: \n" + settings.ini.map{(k,v)-> " > $k -> $v"}.joinToString("\n"))
    val cnn_attr = Connection(settings["connectionString"], settings["APIKey"])
    val lingq = Connector()
    val file = settings["file_name"]
    val pathfile = "$path\\$file"
    val lang_code = settings["lang_code"]
    
    // --------------------------------- Test connection to LingQ API --------------------------------------------------
    print("Test connection ... ")
    if(lingq.isConnectionOK(cnn_attr)) println("OK")
    else {println("SOMETHING WRONG!"); System.exit(-1)}

    // --------------------------------- Display a list of languages ---------------------------------------------------
    print("LingQ languages and known words: ")
    val languages = lingq.getListOfLanguages(cnn_attr)
    if(languages.size==0) println(" NONE")
    else println("\n" + languages.map{" > $it"}.joinToString("\n"))
    
    // --------------------------------- Download words definitions ----------------------------------------------------
    val my_words = if(settings["words_src"]=="lingq") {
        print("Download words from LingQ, pages: ")
        lingq.getListOfWords(cnn_attr,lang_code,settings["max_pages"].toInt())
    } else {
        print("Read $pathfile...")
        loadWordsFromFile(pathfile)
    }
    println(" ${my_words.size} words")
   
   // --------------------------------- Fix word's capitalization ------------------------------------------------------
    val transf_words = if(settings["transform_words"]=="yes") {
        print("Get words from the example sentences...")
        lingq.transformWords(my_words)
    } else my_words
    
    // --------------------------------- Save words to a *.txt file ----------------------------------------------------
    if(settings["save_results_to_file"]=="yes") {
            one_more_time = false
            do {
                print("Save to $pathfile... ")
                val saved = saveFile(transf_words,pathfile)
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
        print("Download mp3 for words... ")
        var saved_files_counter = 0
        for(word in transf_words) {
            var stopit = false
            val mp3filename: String = wordToFilename(word.w)
            do {
                val saved = downloadGooleAudio("$path\\mp3\\$mp3filename",lang_code,word.w,false)
                if(saved!="OK") {
                    print("\n something was wrong with $mp3filename, try one more time? y/n \n" +
                            " or press x to abort downloading ___")
                    val reply: String = readLine()?.uppercase() ?: "N"
                    println("REPLY: [$reply]")
                    one_more_time = if(reply=="Y") true else false
                    stopit = if(reply=="X") true else false
                } else saved_files_counter++
            } while(saved!="OK" && one_more_time==true)
            if(stopit) break
        }
        println("\n $saved_files_counter files saved")
    }
    
    //--------------------------------- Generate HTML file -------------------------------------------------------------
    if(transf_words.size>0 && settings["generate_html"]=="yes") {
        val SPLIT_LIMIT = 10000
        val html_filename = settings["file_name_html"]
        print("Generate HTML file $html_filename ... ")
        if(transf_words.size>SPLIT_LIMIT) {
            println(" \n there are too many words, html file will be split:")
            val first_letters = transf_words.mapNotNull{it.w.firstOrNull()?.uppercaseChar()}.distinct().sorted().filter{it.isLetter()}
            first_letters.forEach {letter->
                val html_pathfile = "$path\\html\\$letter" + "_" + html_filename
                val select = transf_words.filter{it.w.firstOrNull()?.uppercaseChar()==letter}
                print("$letter:${select.size}")
                val save_html_res = saveToHTMLfile(select,html_pathfile)
                print(":$save_html_res ")
            }
        } else {
            val html_pathfile = "$path\\html\\" + html_filename
            val save_html_res = saveToHTMLfile(transf_words,html_pathfile)
            print(save_html_res)
        }
        println("\n .")
    }
    
    
    
}