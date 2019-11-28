package ofat.my.ofat.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (var id: Long?,
                 var login: String?,
                 var email: String?,
                 var firstName: String?,
                 var lastName: String?,
                 var birthDate: String?,
                 var middleName: String?,
                 var password: String?,
                 var salary: Double?,
                 var percent: Double?): Parcelable, BaseObservable() {

    constructor() : this (null, "", "", "", "", "", "", "", null, null)

    @Bindable
    fun getFio(): String {
        return "$lastName $firstName $middleName"
    }
}