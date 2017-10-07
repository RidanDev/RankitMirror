package com.example.gianlucanadirvillalba.mirrorpoll

/**
 * Created by gianlucanadirvillalba on 20/09/2017.
 */
class UrlEndPoints
{
    companion object
    {
        const val URL_RANKIT = "http://www.sapienzaapps.it/rateitapp/"
        const val GET_POLLS = "getpolls.php" //ottengo tutti i polls o uno specifico con id, mi da anche l'ordine dei risultati e numero voti
        const val GET_CANDIDATES = "getcandidates.php" //candidati del pollid
        const val GET_RESULTS = "getresults.php" //ordine risultati e numero voti (dovrebbe bastarmi getpolls)
        const val URL_CHAR_QUESTION = "?"
        const val POLL_ID = "pollid="
    }
}