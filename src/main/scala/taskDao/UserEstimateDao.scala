package taskDao
import org.apache.spark.sql.{DataFrame, SparkSession}
trait UserEstimateDao {
//先对数据归一化
    // 然后相关分析 看看哪个数据对target影响大
    def analysis(spark:SparkSession,dt:DataFrame):Unit
    def fit(spark:SparkSession,df:DataFrame):Unit
    def eval(spark:SparkSession,df:DataFrame):Unit
}
