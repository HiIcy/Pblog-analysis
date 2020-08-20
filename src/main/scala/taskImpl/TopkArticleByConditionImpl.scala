package taskImpl

import constant.Constants
import org.apache.spark.sql.SparkSession

import taskDao.TopkArticleByConditionDao

import com.alibaba.fastjson.JSONObject


class TopkArticleByConditionImpl extends TopkBase with TopkArticleByConditionDao{
    override def process(hc: SparkSession, params: JSONObject): Unit = {
        var articleFields = Array[String]("title","url","user_name")
        super.TopkByCondition(hc,params)(articleFields,Constants.ARTICLE_TABLE_NAME)
    }
}
