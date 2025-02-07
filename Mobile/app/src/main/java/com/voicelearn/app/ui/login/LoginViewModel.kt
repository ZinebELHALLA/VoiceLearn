package com.voicelearn.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voicelearn.app.api.requests.LoginRequest
import com.voicelearn.app.api.responses.ResponseLogin
import com.voicelearn.app.data.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        val request = LoginRequest(email, password)
        repository.login(request)
            .enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        responseBody?.msg.let {
                            _toastText.value = it
                        }

                        responseBody?.name?.let {
                            saveSignedInUserName(it)
                        }

                        _isSucceed.value = true
                        _isLoading.value = false
                    } else {
                        _toastText.value = "Failed: Invalid email or password"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    _toastText.value = "Failed: ${t.message}"
                    _isLoading.value = false
                }

            })
    }

    fun saveSignedInUserName(username: String) {
        repository.saveSignedInUserName(username)
    }
}