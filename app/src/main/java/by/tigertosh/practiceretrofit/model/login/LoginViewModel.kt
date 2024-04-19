package by.tigertosh.practiceretrofit.model.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val token = MutableLiveData<String>()

}