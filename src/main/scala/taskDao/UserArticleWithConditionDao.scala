package taskDao
import org.apache.spark.sql.SparkSession
import com.alibaba.fastjson.JSONObject

trait UserArticleWithConditionDao {
    def process(hc:SparkSession,params:JSONObject)
}
