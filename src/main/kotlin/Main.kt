fun main(args : Array<String>) {
    
    println("\n   -=< LingQ Words Export >=-   \n")
    
    if(args.isNotEmpty()) println("No args accepted, use config.ini")
    
    val path = System.getProperty("user.dir")
    println("Working Directory: $path")
    
    val settings = Config(path)
    println("Settings from config.ini: \n" + settings.ini.map{(k,v)-> " > $k -> $v"}.joinToString("\n"))
    val cnn_attr = Connection(settings["connectionString"], settings["APIKey"])
    val lingq = Connector()
    
    print("Test connection ... ")
    if(lingq.isConnectionOK(cnn_attr)) println("OK")
    else {println("SOMETHING WRONG!"); System.exit(-1)}

    val languages = lingq.getListOfLanguages(cnn_attr)
    println("LingQ languages and known words:\n" + languages.map{" > $it"}.joinToString("\n"))
    
    print("Downloading words from LingQ, pages: ")
    val my_words = lingq.getListOfWords(cnn_attr,settings["lang_code"],settings["max_pages"].toInt())
    println("\n Words: ${my_words.size}")
    
    val transf_words = if(settings["transform_words"]=="yes") {
        print("Getting words from the example sentences...")
        lingq.transformWords(my_words)
    } else my_words
    
    if(transf_words.size>0) {
        val file = settings["file_name"]
        val pathfile = "$path\\$file"
        var one_more_time = false
        do {
            print("Saving to $pathfile... ")
            val saved = saveFile(transf_words,pathfile)
            println(saved)
            if(saved!="OK") {
                print(" saving wasn't OK, try one more time? y/n ")
                val reply: String = readLine()?.uppercase() ?: "N"
                println("REPLY: [$reply]")
                one_more_time = if(reply=="Y") true else false
            }
        } while(saved!="OK" && one_more_time==true)
        
    } else println("Nothing to save.")
}