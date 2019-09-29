package ofat.my.ofat.ui.main.bookkeeping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ofat.my.ofat.model.ShortView

class BookkeepersViewModel : ViewModel() {

    var queryGood: MutableLiveData<ShortView> = MutableLiveData()

    var queryUser: MutableLiveData<ShortView> = MutableLiveData()
}