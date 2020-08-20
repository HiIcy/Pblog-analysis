import com.alibaba.fastjson.JSON
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SaveMode, SparkSession}
//import org.apache.spark.sql.hive.HiveContext
import utils.ParamUtil
import constant.Constants
import taskDao.factory.DAOFactory
import java.util.Date
import scala.util.parsing.json.JSONObject
object main {
    Logger.getLogger("org").setLevel(Level.ERROR)
    def main(args: Array[String]): Unit = {
//                System.setProperty("hadoop.home.dir", "D:\\hiicy\\Download\\Spark大型电商项目实战软件包\\jdk\\hadoop-2.5.0")
        //        //    spark 验证代码
        /*val conf = new SparkConf()
              .setAppName("net work Count")
            //    StreamingExamples.setStreamingLogLevels()
        val sc = new SparkContext(conf)
        val rdd = sc.parallelize(List(1,2,5,4,3,10)).map(_*3)
        val maprdd = rdd.filter(_>10).collect()
        for(arg <- maprdd) {
            print("\n\n"+arg + " \n")
        }
        sc.stop()
        System.exit(0)*/

        val hc = SparkSession.builder()
            .appName("pblog analysis")
            .enableHiveSupport()
            .getOrCreate()

        var taskCategoryId = ParamUtil.getTaskCategoryFromArgs(args).getOrElse(-1)
        if (taskCategoryId < 0) {
            println("没有正确的任务类别! ")
            System.exit(0)
        }
        var params = ParamUtil.parseArgs(args).getOrElse("")
        if (params == ""){
            println("没有足够的参数! ")
            System.exit(0)
        }
        val parameters = JSON.parseObject(params)
        taskCategoryId match {
            case x if x==0 =>
                hc.sql("use pblog")
                val topkUserByConditionDao = DAOFactory.getTopkUserByConditionDao
                topkUserByConditionDao.process(hc,parameters)
            case x if x==1 =>
                val topkArticleByConditionDao = DAOFactory.getTopkArticleByConditionDao
                topkArticleByConditionDao.process(hc,parameters)
            case x if x==2 =>
                val userWithArticleByConditionDao = DAOFactory.getUserArticleWithConditionDao
                userWithArticleByConditionDao.process(hc,parameters)
            case x if x==3 =>
                val userEstimateDao = DAOFactory.getUserEstimateDao
                hc.sql("use pblog")
                val dt = hc.sql("select * from user_info")
                userEstimateDao.analysis(hc,dt)
                Thread.sleep(10)
                userEstimateDao.fit(hc,dt)
        }
        hc.stop()
    }
}