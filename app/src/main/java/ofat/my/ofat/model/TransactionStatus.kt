package ofat.my.ofat.model

enum class TransactionStatus(val description: String) {

    ACCEPTED("Подтверждена"),
    NOT_ACCEPTED("Не подтверждена"),
    DELAYED("Отложена"),
    CANCELED("Отменена");
}