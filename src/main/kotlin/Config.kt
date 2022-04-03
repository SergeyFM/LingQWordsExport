class Config {
    // reads config.ini
    val ini: Map<String,String>
    init {
        //println("Config.ini:")
        val txt: String = this::class.java.classLoader.getResource("config.ini").readText()
        ini = txt.split("\n").mapNotNull{line->
            //println(" > $line")
            val two = line.split("=")
            if(two.first().length>0 && two.last().length>0 && two.size==2)
                Pair(two.first().trim(),two.last().trim())
            else null
        }.toMap()
     }
    operator fun get(k: String): String = ini[k] ?: ""
}