package by.tigertosh.practiceretrofit.model.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.tigertosh.practiceretrofit.R
import by.tigertosh.practiceretrofit.databinding.FragmentLoginBinding
import by.tigertosh.practiceretrofit.network.MainApi
import by.tigertosh.practiceretrofit.network.data.AuthRequest
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainApi: MainApi
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        binding.apply {
            buttonEnter.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            }
            buttonRegistration.setOnClickListener {
                auth(AuthRequest(
                    loginText.text.toString(),
                    passwordText.text.toString()
                ))
            }
        }

    }

    private fun initRetrofit() {

        val interception = HttpLoggingInterceptor()
        interception.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interception)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
       mainApi = retrofit.create(MainApi::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            val messError = response.errorBody()?.string()?.let { JSONObject(it)
                .getString("message") }
            requireActivity().runOnUiThread {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = messError
                val user = response.body()
                if (user != null) {
                    Picasso.get().load(user.image).into(binding.imageView)
                    binding.userName.text = user.firstName
                    viewModel.token.value = user.token
                    binding.buttonEnter.visibility = View.VISIBLE
                    binding.buttonRegistration.visibility = View.GONE
                    binding.loginText.visibility = View.GONE
                    binding.passwordText.visibility = View.GONE
                }
            }
        }
    }


}