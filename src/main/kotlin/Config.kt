import java.io.File
import java.lang.Exception

class Config (val path: String) {
    // reads config.ini
    val ini: Map<String,String>
    init {
        var txt = ""
        try {
            val f = File("$path\\config.ini")
            txt = f.readText()
        } catch(ex: Exception) {
            println("ERROR: " + ex.message)
            System.exit(-1)
        }
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