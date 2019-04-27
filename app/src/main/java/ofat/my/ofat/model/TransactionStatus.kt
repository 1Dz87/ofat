package ofat.my.ofat.model

enum class TransactionStatus(description: String) {

    ACCEPTED("Подтверждена"),
    NOT_ACCEPTED("Не подтверждена"),
    DELAYED("Отложена"),
    CANCELED("Отменена");
}