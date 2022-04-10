import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Connector {
    // Responsible for all LingQ communications
    fun isConnectionOK(cnn: Connection): Boolean {
        try {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(cnn.connectionString+"v2/contexts/"))
            .header("Authorization",  "Token " + cnn.APIKey)
            .build();
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val ret = response.body()
        return "knownWords" in ret
        } catch(ex: Exception) {
            println("ERROR: " + ex)
            return false
        }
    }
    
    fun getListOfLanguages(cnn: Connection): List<String> {
        // returns the list of languages with amount of known words
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(cnn.connectionString+"v2/contexts/"))
            .header("Authorization",  "Token " + cnn.APIKey)
            .build();
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val ret = response.body()
        return ret.split("{").filter{"knownWords" in it}
            .map{line->
                "id:" + getJSONproperty("id",line) + " code:" +
                getJSONproperty("code",line) + " \t" +
                getJSONproperty("title",line) + " (" +
                getJSONproperty("knownWords", line) + ")"
        }
    }

    data class Word (
        val w: String,  // the word
        val t: String,  // a translation
        val f: String,  // an example sentence, fragment
        val s: Int,     // status 0-3
        val n: Int) {   // numbers of words in the word (more than one means a sentence)
        override fun toString() = this::class.java.declaredFields.filter{it.name!="INSTANCE"}
            .map{it.get(this).toString()}.joinToString("\t")
    }
    
    
    fun getListOfWords(cnn: Connection, lang_code: String, pages_limit: Int): List<Word> {
        // returns the list of words from LingQ: Word, Translation, Status
        if(pages_limit<1) return listOf<Word>()
        val words = mutableListOf<Word>()
        val client = HttpClient.newBuilder().build()
        for(page in (1..pages_limit)) {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(cnn.connectionString+"v2/$lang_code/cards/?page_size=25&page=$page"))
                .header("Authorization","Token "+cnn.APIKey)
                .header("Content-Type", "application/json; charset=utf-8")
                .build()
            val response = client.send(request,HttpResponse.BodyHandlers.ofString())
            val ret = response.body()
            if("""{"detail":"Invalid page."}""" in ret || ret.length<30) break
            print("|$page" + if(page%23==0) "\n" else "")
            ret.replace("\t"," ").split("\"pk\":").filter{"text" in it}.forEach {w_def->
                val term = unEscapeUnicode(getJSONproperty("term",w_def)).trim()
                val new_word = Word(
                    term,
                    unEscapeUnicode(getJSONproperty("text",w_def)),
                    unEscapeUnicode(getJSONproperty("fragment",w_def)),
                    getJSONproperty("status",w_def).toIntOrNull()?:0,
                    term.count{it==' '}+1
                )
                if(new_word.w.length>0) words += new_word
            }
        }
        print("|")
        return words
    }
    
    fun transformWords(words: List<Word>): List<Word> {
        // take the words from example sentences, for LingQ is case-insensitive
        var counter = 0
        val transformed_words = words.map {word->
            val word_to_try = replaceSomeCharacters(word.w)
            val new_word = takeWordFromSentence(word_to_try,word.f)
            if(word.w!=new_word) counter++
            Word(new_word,word.t,word.f,word.s,word.n)
        }
        println(" $counter changed. ")
        return transformed_words
    }
//companion object {
    private fun takeWordFromSentence(w:String, s:String): String {
        // finds case-insensitive w_ord in the s_entence, gets it from there, or returns w_ord
        val ind = s.indexOf(w,0,true)
        if(ind<0) return w
        for(i in (ind-1 downTo 0)) when(s[i]) {
            ' ' -> continue
            '.' -> return w
            else -> break
        }
        return s.substring(ind..w.length+ind-1)
    }
    
    private fun replaceSomeCharacters(w:String): String {
        val to_replace = mapOf("ﬂ" to "fl")
        return w.map {c->
            to_replace["$c"]?:"$c"
        }.joinToString("")
    }
//}

}