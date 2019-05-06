package ofat.my.ofat.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService



class MainFragment : androidx.fragment.app.Fragment() {

    private lateinit var login: TextInputEditText

    private lateinit var password: TextInputEditText

    private lateinit var btLogin: Button

    private lateinit var btExit: Button

    private lateinit var viewModel: MainViewModel

    private lateinit var progress: ProgressBar

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
            val request: MutableMap<String, String> = mutableMapOf()
            if (loginValue != null && passValue != null) {
                request["login"] = loginValue
                request["password"] = passValue
                password.text = null
                hideKeyboardFrom(context!!, view!!)
                loginProcess(request)
            } else {
                Toast.makeText(context, "Не заполнены обязательные поля.", Toast.LENGTH_SHORT).show()
            }
        }
        btExit.setOnClickListener {
            this.activity?.finish()
            System.exit(0)
        }
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun loginProcess(request: MutableMap<String, String>) {
        val call = OfatApplication.authApi?.login(request)
        UtilUI.showProgress(progress)
        call?.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                UtilUI.showProgress(progress)
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigate(R.id.menuFragment)
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                UtilUI.showProgress(progress)
                if((response.body() as AuthResponse).success != null) {
                    val user = (response.body() as AuthResponse).success!!
                    OfatApplication.currentUser = user
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
}

