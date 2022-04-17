import java.io.File
import java.lang.Exception
import kotlin.system.exitProcess

class Config (val path: String) {
    // reads config.ini
    val ini: MutableMap<String,String>
    init {
        var txt = ""
        try {
            val f = File("$path\\config.ini")
            txt = f.readText()
        } catch(ex: Exception) {
            println("ERROR: $ex")
            exitProcess(-1)
        }
        ini = txt.split("\n").filter{"=" in it}.mapNotNull{line->
            val two = line.split("=")
            if(two.first().length>0)
                Pair(two.first().trim(),two.last().trim().takeWhile{it !in " '"})
            else null
        }.toMap().toMutableMap()
        // get my test api key
        if(ini["APIKey"].isNullOrBlank()) ini["APIKey"] = readFile("$path\\LINGQ_API.KEY")
     }
    operator fun get(k: String): String = ini[k] ?: ""
}