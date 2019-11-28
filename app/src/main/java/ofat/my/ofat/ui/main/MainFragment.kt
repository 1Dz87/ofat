package ofat.my.ofat.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.AuthResponse
import okhttp3.Credentials
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainFragment : androidx.fragment.app.Fragment() {

    private lateinit var login: TextInputEditText

    private lateinit var password: TextInputEditText

    private lateinit var btLogin: Button

    private lateinit var btExit: Button

    private lateinit var viewModel: MainViewModel

    private lateinit var progress: ProgressBar

    private lateinit var creds: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        defineUi(view)
        (activity as MainActivity).setCurrentTitle(R.string.app_name)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun defineUi(view: View) {
        login = view.findViewById(R.id.login_edit_text)
        password = view.findViewById(R.id.password_edit_text)
        btLogin = view.findViewById(R.id.home_login)
        btExit = view.findViewById(R.id.home_exit)
        progress = view.findViewById(R.id.progressBar)
        bindButtonActions()
    }

    private fun bindButtonActions() {
        btLogin.setOnClickListener {
            val loginValue = ExtractUtil.v(login)
            val passValue = ExtractUtil.v(password)
            if (loginValue != null && passValue != null) {
                creds = Credentials.basic(loginValue, passValue)
                OfatApplication.httpClient = makeHttpClient()
                apiCreation()
                loginProcess()
                password.text = null
                hideKeyboardFrom(context!!, view!!)
            } else {
                Toast.makeText(context, "Не заполнены обязательные поля.", Toast.LENGTH_SHORT).show()
            }
        }
        btExit.setOnClickListener {
            this.activity?.finish()
            System.exit(0)
        }
    }

    private fun loginProcess() {
        val call = OfatApplication.authApi?.login(creds)
        UtilUI.showProgress(progress)
        call?.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                UtilUI.showProgress(progress)
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigate(R.id.menuFragment)
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                UtilUI.showProgress(progress)
                if ((response.body() as AuthResponse).success != null) {
                    val user = (response.body() as AuthResponse).success
                    OfatApplication.creds = creds
                    viewModel.user.postValue(user)
                    (activity as MainActivity).changeMenuNavigatorOnLogin()
                    view?.findNavController()?.navigate(R.id.menuFragment)
                } else {
                    if ((response.body() as AuthResponse).errors != null) {
                        Toast.makeText(context, (response.body() as AuthResponse).errors, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun makeHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor {
            val originalRequest = it.request()
            val builder = originalRequest.newBuilder().header(
                "Authorization",
                creds
            )
            val newRequest = builder.build()
            it.proceed(newRequest)
        }.build()
    }

    private fun apiCreation() {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(OfatApplication.httpClient!!)
        retrofitBuilder.addConverterFactory(gsonConverterFactory())
        val serverUrl = "192.168.0.103:8080"
        val httpUrl = HttpUrl.parse("http://$serverUrl/ofat/")
        retrofitBuilder.baseUrl(httpUrl!!)
        val retrofit = retrofitBuilder.build()
        OfatApplication.makeApis(retrofit)
    }

    private fun gsonConverterFactory(): Converter.Factory {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }
}

