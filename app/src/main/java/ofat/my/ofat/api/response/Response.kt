package ofat.my.ofat.api.response

import ofat.my.ofat.model.*


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

class GoodsGroupResponse: Response() {
    var success: GoodsGroup? = null
    var errors: String? = null
}

class GoodsGroupNamesResponse: Response() {
    var success: List<GoodsGroup>? = null
    var errors: String? = null
}

class GoodsGroupSVResponse: Response() {
    var success: List<ShortView>? = null
    var errors: String? = null
}

class TransactionResponse: Response() {
    var success: Map<String, Transaction>? = null
    var errors: String? = null
}

class GetBookkeepersSVResponse: Response() {
    var success: List<ShortView>? = null
    var errors: String? = null
}

class CreateBookkeepersResponse: Response() {
    var success: Long? = null
    var errors: String? = null
}

class GetPointsResponse: Response() {
    var success: List<Point>? = null
    var errors: String? = null
}