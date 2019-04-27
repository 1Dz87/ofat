package ofat.my.ofat.Util

object CollectionUtils {

    fun isNotEmpty(collection: MutableCollection<*>?) : Boolean {
        return collection != null && collection.isNotEmpty()
    }

    fun isEmpty(collection: MutableCollection<*>?) : Boolean {
        return !isNotEmpty(collection)
    }

    fun mapIsNotEmpty(map: MutableMap<*, *>?) : Boolean {
        return map != null && map.isNotEmpty()
    }

    fun mapIsEmpty(map: MutableMap<*, *>?) : Boolean {
        return !mapIsNotEmpty(map)
    }
}