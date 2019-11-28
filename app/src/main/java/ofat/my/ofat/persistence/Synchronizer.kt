package ofat.my.ofat.persistence

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.api.response.GoodsGroupNamesResponse
import ofat.my.ofat.api.response.GoodsSyncResponse
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.GoodsGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Synchronizer(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val goodGroupsSyncResult = synchronizeGoodGroups(applicationContext)
        val goodSyncResult = synchronizeGoods(applicationContext)
        return if (goodSyncResult && goodGroupsSyncResult) Result.success() else Result.failure()
    }

    private fun synchronizeGoodGroups(context: Context) : Boolean {
        val call = OfatApplication.goodsGroupApi?.synchronizeGroups()
        var result = false
        call?.enqueue(object : Callback<GoodsGroupNamesResponse>{
            override fun onFailure(call: Call<GoodsGroupNamesResponse>, t: Throwable) {
                //ignore
            }

            override fun onResponse(call: Call<GoodsGroupNamesResponse>, response: Response<GoodsGroupNamesResponse>) {
                if (response.body() != null && response.body()?.success != null) {
                    val goods = response.body()?.success as List<GoodsGroup>
                    goods.forEach {
                        val group = OfatDatabase.getInstance(context).goodsGroupDao().getById(it.id!!)
                        if (group == null) {
                            OfatDatabase.getInstance(context).goodsGroupDao().save(it)
                        } else {
                            OfatDatabase.getInstance(context).goodsGroupDao().update(it)
                        }
                    }
                    result = true
                }
            }

        })
        return result
    }

    private fun synchronizeGoods(context: Context) : Boolean {
        val call = OfatApplication.goodApi?.sync()
        var result = false
        call?.enqueue(object : Callback<GoodsSyncResponse>{
            override fun onFailure(call: Call<GoodsSyncResponse>, t: Throwable) {
                //ignore
            }

            override fun onResponse(call: Call<GoodsSyncResponse>, response: Response<GoodsSyncResponse>) {
                if (response.body() != null && response.body()?.success != null) {
                    val goods = response.body()?.success as List<Good>
                    goods.forEach {
                        val good = OfatDatabase.getInstance(context).goodDao().getById(it.id!!)
                        if (good == null) {
                            OfatDatabase.getInstance(context).goodDao().save(it)
                        } else {
                            OfatDatabase.getInstance(context).goodDao().update(it)
                        }
                    }
                    result = true
                }
            }

        })
        return result
    }
}