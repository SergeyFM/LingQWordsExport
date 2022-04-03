fun main(args: Array<String>) {
    println("\n   -=< LingQ Words Export >=-   \n")
    val settings = Config()
    println("Settings from config.ini: \n" + settings.ini.map{(k,v)-> " > $k -> $v"}.joinToString("\n"))
    val cnn_attr = Connection(settings["connectionString"], settings["APIKey"])
    val lingq = Connector()

    val languages = lingq.getListOfLanguages(cnn_attr)
    println("LingQ languages and known words:\n" + languages.map{" > $it"}.joinToString("\n"))
    
    print("Download words from the LingQ.\n Processing pages ... ")
    val my_words = lingq.getListOfWords(cnn_attr,settings["lang_code"],settings["max_pages"].toInt())
    println("\n Words: ${my_words.size}")
    
    val transf_words = if(settings["transform_words"]=="yes") {
        print("Getting words from the example sentences...")
        lingq.transformWords(my_words)
    } else my_words
    
    if(transf_words.size>0) {
        val pathfile = settings["wordlist_file"]
        print("Saving to $pathfile... ")
        val saved = saveFile(transf_words,pathfile)
        println("OK")
    } else println("Nothing to save.")
}