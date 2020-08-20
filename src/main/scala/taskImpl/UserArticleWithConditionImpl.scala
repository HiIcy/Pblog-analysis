package taskImpl
import constant.Constants
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import taskDao.UserArticleWithConditionDao
import com.alibaba.fastjson.JSONObject

class UserArticleWithConditionImpl extends TopkBase with UserArticleWithConditionDao{
    override def process(hc: SparkSession, params: JSONObject): Unit = {
        val user_name = params.getString("user_name")
        var articleFields = Array[String]("title","url","user_name")
        var articles:DataFrame = hc.sql(f"select * from article_info where user_name=$user_name")
        this.TopkByCondition(hc,params)(articleFields,Constants.ARTICLE_TABLE_NAME)
    }
}
