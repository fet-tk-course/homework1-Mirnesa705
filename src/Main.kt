interface Osoba {
    fun Identitet(): String
    fun Titula(): String
}

open class Inzenjer(
    val ime: String,
    val prezime: String,
    val titula: String,
    var godineIskustva: Int,
    var ekspertize: Set<String>
) : Osoba {
    
    init {
        require(ime.isNotBlank()) { "Ime ne smije biti prazno" }
        require(prezime.isNotBlank()) { "Prezime ne smije biti prazno" }
        require(titula.isNotBlank()) { "Titula ne smije biti prazna" }
        require(godineIskustva >= 0) { "Godine iskustva moraju biti >= 0" }
        require(ekspertize.isNotEmpty()) { "Lista ekspertiza ne smije biti prazna" }
    }
    
    override fun Identitet(): String = "$ime $prezime"
    
    override fun Titula(): String = titula
    
    open fun prikaziInformacije(): String {
        return "${Identitet()} - ${Titula()} - $godineIskustva godina - Ekspertize: ${ekspertize.joinToString(", ")}"
    }
}


class SoftverskirInzenjer(
    ime: String,
    prezime: String,
    godineIskustva: Int,
    ekspertize: Set<String>,
    var brojProjekata: Int
) : Inzenjer(ime, prezime, "Softverski inženjer", godineIskustva, ekspertize) {
    
    init {
        require(brojProjekata >= 0) { "Broj projekata mora biti >= 0" }
    }
    
    fun izracunajUspjesnost(): Double {
        return if (godineIskustva > 0) brojProjekata.toDouble() / godineIskustva else 0.0
    }
    
    override fun prikaziInformacije(): String {
        return "${super.prikaziInformacije()} - Projekti: $brojProjekata (Uspješnost: ${"%.2f".format(izracunajUspjesnost())} projekata/godini)"
    }
}

class InzenjerElektrotehnike(
    ime: String,
    prezime: String,
    godineIskustva: Int,
    ekspertize: Set<String>,
    var brojCertifikata: Int
) : Inzenjer(ime, prezime, "Inženjer elektrotehnike", godineIskustva, ekspertize) {
    
    init {
        require(brojCertifikata >= 0) { "Broj certifikata mora biti >= 0" }
    }
    
    fun izracunajUspjesnost(): Double {
        return if (godineIskustva > 0) brojCertifikata.toDouble() / godineIskustva else 0.0
    }
    
    override fun prikaziInformacije(): String {
        return "${super.prikaziInformacije()} - Certifikati: $brojCertifikata (Uspješnost: ${"%.2f".format(izracunajUspjesnost())} certifikata/godini)"
    }
}

fun <K, V, R> Map<K, V>.aggregate(
    initial: R,
    operation: (acc: R, entry: Map.Entry<K, V>) -> R
): R {
    return this.entries.fold(initial, operation)
}

fun grupisanjePoEkspertizama(inzenjeri: List<Inzenjer>): Map<String, List<Inzenjer>> {
    return inzenjeri.fold(mutableMapOf<String, MutableList<Inzenjer>>()) { akumulator, inzenjer ->
        if (inzenjer.godineIskustva > 5) {
            inzenjer.ekspertize.forEach { ekspertiza ->
                akumulator.getOrPut(ekspertiza) { mutableListOf() }.add(inzenjer)
            }
        }
        akumulator
    }
}

fun najiskusnijiPoTipu(inzenjeri: List<Inzenjer>): Map<String, Inzenjer> {
    val rezultat = mutableMapOf<String, Inzenjer>()
    
    val softverski = inzenjeri.filterIsInstance<SoftverskirInzenjer>()
    if (softverski.isNotEmpty()) {
        rezultat["Softverski inženjer"] = softverski.reduce { najiskusniji, trenutni ->
            if (trenutni.godineIskustva > najiskusniji.godineIskustva) trenutni else najiskusniji
        }
    }
    
    val elektrotehnicki = inzenjeri.filterIsInstance<InzenjerElektrotehnike>()
    if (elektrotehnicki.isNotEmpty()) {
        rezultat["Inženjer elektrotehnike"] = elektrotehnicki.reduce { najiskusniji, trenutni ->
            if (trenutni.godineIskustva > najiskusniji.godineIskustva) trenutni else najiskusniji
        }
    }
    
    return rezultat
}

fun ukupneVrijednosti(inzenjeri: List<Inzenjer>): Int {
    val grupisaniPoTipu = inzenjeri.groupBy { it::class.simpleName }
    
    return grupisaniPoTipu.aggregate(0) { ukupno: Int, entry: Map.Entry<String?, List<Inzenjer>> ->
        val (tip, lista) = entry
        when (tip) {
            "SoftverskirInzenjer" -> {
                ukupno + lista.filterIsInstance<SoftverskirInzenjer>()
                    .sumOf { it.brojProjekata }
            }
            "InzenjerElektrotehnike" -> {
                ukupno + lista.filterIsInstance<InzenjerElektrotehnike>()
                    .sumOf { it.brojCertifikata }
            }
            else -> ukupno
        }
    }
}

fun ispisiSveInzenjere(inzenjeri: List<Inzenjer>) {
    println("\n=== LISTA SVIH INŽENJERA ===\n")
    inzenjeri.forEachIndexed { index, inzenjer ->
        println("${index + 1}. ${inzenjer.prikaziInformacije()}")
    }
}

fun ispisiGrupuPoEkspertizama(grupe: Map<String, List<Inzenjer>>) {
    println("\n=== GRUPISANJE PO EKSPERTIZAMA (>5 god iskustva) ===\n")
    grupe.forEach { (ekspertiza, lista) ->
        println("Ekspertiza: $ekspertiza (${lista.size} inženjera)")
        lista.forEach { inzenjer ->
            println("  - ${inzenjer.Identitet()} (${inzenjer.godineIskustva} godina)")
        }
        println()
    }
}

fun ispisiNajiskusnije(najiskusniji: Map<String, Inzenjer>) {
    println("\n=== NAJISKUSNIJI PO TIPU ===\n")
    najiskusniji.forEach { (tip, inzenjer) ->
        println("$tip: ${inzenjer.Identitet()} - ${inzenjer.godineIskustva} godina iskustva")
    }
}


fun main() {

    val inzenjeri = listOf(
        SoftverskirInzenjer("Amina", "Hodžić", 8, setOf("Kotlin", "Android", "Java"), 15),
        SoftverskirInzenjer("Emir", "Selimović", 4, setOf("Python", "Django", "React"), 8),
        SoftverskirInzenjer("Lejla", "Karić", 12, setOf("Java", "Spring", "Kotlin"), 25),
        SoftverskirInzenjer("Faruk", "Ibrahimović", 6, setOf("JavaScript", "Node.js", "React"), 12),
        InzenjerElektrotehnike("Tarik", "Mujić", 10, setOf("Elektronika", "Mikrokontroleri"), 8),
        InzenjerElektrotehnike("Selma", "Begić", 3, setOf("Energetika", "Obnovljivi izvori"), 2),
        InzenjerElektrotehnike("Nermin", "Softić", 15, setOf("Telekomunikacije", "5G", "IoT"), 12),
        InzenjerElektrotehnike("Dženana", "Omerović", 7, setOf("Automatika", "PLC", "SCADA"), 5)
    )
    
    ispisiSveInzenjere(inzenjeri)
    
    val grupisaniPoEkspertizama = grupisanjePoEkspertizama(inzenjeri)
    ispisiGrupuPoEkspertizama(grupisaniPoEkspertizama)
    
    val najiskusniji = najiskusnijiPoTipu(inzenjeri)
    ispisiNajiskusnije(najiskusniji)
    
    val ukupno = ukupneVrijednosti(inzenjeri)
    println("\n=== UKUPNE VRIJEDNOSTI ===\n")
    println("Ukupan broj projekata i certifikata: $ukupno")
    println("  - Projekti (softverski): ${inzenjeri.filterIsInstance<SoftverskirInzenjer>().sumOf { it.brojProjekata }}")
    println("  - Certifikati (elektrotehnički): ${inzenjeri.filterIsInstance<InzenjerElektrotehnike>().sumOf { it.brojCertifikata }}")

}
