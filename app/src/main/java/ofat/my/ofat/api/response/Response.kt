package ofat.my.ofat.api.response

import ofat.my.ofat.model.Good
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.model.Transaction
import ofat.my.ofat.model.User


open class Response

class AuthResponse: Response() {
    var success: User? = null
    var errors: String? = null
}

class CreateUserResponse: Response() {
    var success: String? = null
    var errors: String? = null
}

class GetGoodResponse: Response() {
    var success: Good? = null
    var errors: String? = null
}

class AddGoodResponse: Response() {
    var success: Map<Long, String>? = null
    var errors: String? = null
}

class GetGoodShortViewResponse: Response() {
    var success: MutableCollection<ShortView>? = null
    var errors: String? = null
}

class DeleteGoodResponse: Response() {
    var success: String? = null
    var errors: String? = null
}

class TransactionResponse: Response() {
    var success: Map<String, Transaction>? = null
    var errors: String? = null
}