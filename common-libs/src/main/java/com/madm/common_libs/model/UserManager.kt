package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*





data class UserManager(
    var context: Context
) {
    private var usersList : UsersList? = null
    private var s: Server = Server(context)


    fun getUsers(callbackFunction: (List<User>) -> Unit) {
        s.makeGetRequest<UsersList>(Server.RequestKind.USERS) { ret ->
            this.usersList = ret

            callbackFunction(this.usersList!!.users)
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
    @IgnoredOnParcel var permanentConstraints: List<PermanentConstraint> = listOf(),
    @IgnoredOnParcel var nonPermanentConstraints: List<NonPermanentConstraint> = listOf()
) : Parcelable



@Parcelize
data class PermanentConstraint(
    @IgnoredOnParcel var dayOfWeek:Int? = null,
    @IgnoredOnParcel var type: String? = null
) : Parcelable

@Parcelize
data class NonPermanentConstraint(
    @IgnoredOnParcel var date: Date? = null,
    @IgnoredOnParcel var type: String? = null
) : Parcelable