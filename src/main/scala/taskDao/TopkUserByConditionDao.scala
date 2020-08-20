package taskDao

//import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.SparkSession
import com.alibaba.fastjson.JSONObject


trait TopkUserByConditionDao {
    def process(hc:SparkSession,params:JSONObject)
}
