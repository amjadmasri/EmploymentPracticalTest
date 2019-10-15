package demo.mindvalleytest.ui.JsonFileDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.mindvalleytest.data.repositories.MVImageRepository
import javax.inject.Inject

class JsonFileViewModel @Inject constructor(repository: MVImageRepository) : ViewModel() {
    private var jsonFileUrl: MutableLiveData<String> =
        MutableLiveData<String>()

    fun getJsonFileUrl(): LiveData<String> {
        jsonFileUrl.value="https://pastebin.com/raw/wgkJgazE"
        return jsonFileUrl
    }
}
