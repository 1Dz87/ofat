package ofat.my.ofat.ui.main.users

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText

import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.UserApi
import ofat.my.ofat.api.response.CreateUserResponse
import ofat.my.ofat.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class UsersFragment : Fragment() {

    companion object {
        fun newInstance() = UsersFragment()
    }

    private lateinit var firstName: TextInputEditText

    private lateinit var lastName: TextInputEditText

    private lateinit var middleName: TextInputEditText

    private lateinit var birthDate: TextInputEditText

    private lateinit var email: TextInputEditText

    private lateinit var login: TextInputEditText

    private lateinit var password: TextInputEditText

    private lateinit var repeatPassword: TextInputEditText

    private lateinit var btCreate: Button

    private lateinit var btCancel: Button

    private lateinit var progress: ProgressBar

    private lateinit var viewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.users_fragment, container, false)
        initFields(view)
        initButtons(view)
        return view
    }

    private fun initFields(view: View) {
        progress = view.findViewById(R.id.user_progressBar)
        firstName = view.findViewById(R.id.first_name_edit_text)
        lastName = view.findViewById(R.id.last_name_edit_text)
        middleName = view.findViewById(R.id.middle_name_edit_text)

        birthDate = view.findViewById(R.id.birth_edit_text)
        UtilUI.setDateToField(birthDate, activity!!)

        email = view.findViewById(R.id.email_edit_text)
        login = view.findViewById(R.id.login_user_edit_text)
        password = view.findViewById(R.id.password_edit_text)
        repeatPassword = view.findViewById(R.id.repeat_password_edit_text)
    }

    private fun initButtons(view: View) {
        btCreate = view.findViewById(R.id.btAddUser)
        btCreate.setOnClickListener {
            if (UtilUI.checkTextFields(arrayOf(firstName, lastName, birthDate, login, password, repeatPassword))) {
                val passwordStr = ExtractUtil.v(password)!!
                val repeatPass = ExtractUtil.v(repeatPassword)!!
                if (passwordStr == repeatPass) {
                    requestProcess(passwordStr)
                } else {
                    password.setText("")
                    password.error = ""
                    repeatPassword.setText("")
                    repeatPassword.error = ""
                    Toast.makeText(view.context, "Не совпадают пароли.", Toast.LENGTH_LONG).show()
                }
            }
        }
        btCancel = view.findViewById(R.id.btBackToMenu)
        btCancel.setOnClickListener { view.findNavController().navigateUp() }
    }

    private fun requestProcess(passwordStr: String) {
        val middleNameStr = ExtractUtil.v(middleName)
        val emailStr = ExtractUtil.v(email)
        val request = User()
        request.firstName = ExtractUtil.v(firstName)!!
        request.lastName = ExtractUtil.v(lastName)!!
        request.middleName = middleNameStr
        request.birthDate = ExtractUtil.v(birthDate)!!
        request.email = emailStr
        request.login = ExtractUtil.v(login)!!
        request.password = passwordStr
        val call = OfatApplication.userApi?.addClient(request)
        UtilUI.showProgress(progress)
        call?.enqueue(object : Callback<CreateUserResponse> {
            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                Toast.makeText(view?.context, "Ошибка сервера", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CreateUserResponse>, response: Response<CreateUserResponse>) {
                if (response.body()?.success != null) {
                    Toast.makeText(view?.context, response.body()?.success, Toast.LENGTH_SHORT).show()
                } else {
                    if (response.body()?.errors != null) {
                        Toast.makeText(view?.context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
    }
}
