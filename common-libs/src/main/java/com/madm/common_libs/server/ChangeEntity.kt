package com.madm.common_libs.server


data class Change(
    var applicantId: String,
    var recipientId: String
){
    fun requestChange(applicantId: String, recipientId: String){
        val s : Server = Server()
        s.makePostRequest<Change>(this, Server.RequestKind.CHANGES)
    }
}

data class ChangeList (var recipientId: String){
    private lateinit var changeList: List<Change>

    private fun loadAllChanges() {
        val s = Server()
        val tmpList: List<Change>? = s.makeGetRequest<List<Change>>(Server.RequestKind.CHANGES)

        if(tmpList != null){
            changeList = tmpList
        }
    }

    fun getRecipientChanges() : List<Change>{
        loadAllChanges()
        return changeList.filter { it.recipientId == this.recipientId || it.recipientId == "ALL" }
    }
}