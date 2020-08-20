package taskImpl

import constant.Constants
//import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.SparkSession
import taskDao.TopkUserByConditionDao
import com.alibaba.fastjson.JSONObject


class TopkUserByConditionImpl extends TopkBase with TopkUserByConditionDao {
    override def process(hc: SparkSession, params: JSONObject): Unit = {
        val userFields = Array[String]("name", "url")
        // 柯里化使用
        this.TopkByCondition(hc,params)(userFields,Constants.USER_TABLE_NAME)
    }
}
