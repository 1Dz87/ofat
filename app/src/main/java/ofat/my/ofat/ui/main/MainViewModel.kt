package ofat.my.ofat.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ofat.my.ofat.R
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.model.User

class MainViewModel : ViewModel() {

    val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val loggedIn: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val redirect: MutableLiveData<Boolean>? by lazy { MutableLiveData(false) }

    val currentGood : MutableLiveData<Good> by lazy { MutableLiveData<Good>() }

    var foundList : MutableLiveData<MutableCollection<ShortView>?> = MutableLiveData(mutableListOf())

    val barcodeForCreate : MutableLiveData<String>? = MutableLiveData()

    val currentTitle : MutableLiveData<Any> = MutableLiveData(R.string.side_menu_close)
}
