package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

data class UserManager(var context: Context) {
    private var usersList : UsersList? = null
    private val s : Server = Server.getInstance(context)

    fun getUsers(callbackFunction: (MutableList<User>) -> Unit) {
        s.makeGetRequest<UsersList>(Server.RequestKind.USERS) { ret ->
            this.usersList = ret

            callbackFunction(this.usersList!!.users.toMutableList())
        }
    }
}

@Parcelize
private data class UsersList(
    @IgnoredOnParcel var users: List<User> = listOf()
) : Parcelable

@Parcelize
data class User(
    @IgnoredOnParcel var id: String? = null,
    @IgnoredOnParcel var name: String? = null,
    @IgnoredOnParcel var surname: String? = null,
    @IgnoredOnParcel var email: String? = null,
    @IgnoredOnParcel var password: String? = null,
    @IgnoredOnParcel var permanentConstraints: ArrayList<PermanentConstraint> = arrayListOf(),
    @IgnoredOnParcel var nonPermanentConstraints: ArrayList<NonPermanentConstraint> = arrayListOf()
) : Parcelable {
    fun registerOrUpdate(context : Context){
        val s : Server = Server.getInstance(context)
        s.makePostRequest<User>(this, Server.RequestKind.USERS)
    }

    fun unregister(context: Context){
        val s : Server = Server.getInstance(context)
        s.makeDeleteRequest<User>(this, Server.RequestKind.USERS)
    }
}

@Parcelize
data class PermanentConstraint(
    @IgnoredOnParcel var dayOfWeek:Int? = null,
    @IgnoredOnParcel var type: String? = null
) : Parcelable

@Parcelize
data class NonPermanentConstraint(
    @IgnoredOnParcel var type: String? = null
) : Parcelable {

    @Transient
    @IgnoredOnParcel
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)

    @IgnoredOnParcel
    var date: String? = null

    @Transient
    @IgnoredOnParcel
    var constraintDate: Date? = null
    set(value){
        field = value
        this.date = dateFormat.format(value!!)
    }
    get() {
        return if(field == null && this.date != null)
            dateFormat.parse(this.date!!)
        else null
    }
}