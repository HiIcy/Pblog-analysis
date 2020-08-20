package taskImpl
import org.apache.spark.sql.SaveMode
import utils.ParamUtil
//import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.SparkSession
import com.alibaba.fastjson.JSONObject
import constant.Constants
trait TopkBase {
    def TopkByCondition(hc:SparkSession,params:JSONObject)(fields:Array[String], table:String):Unit={
        import hc.implicits._
        val topK = params.getString("topk").toInt
        var parameters = ParamUtil.getConcatStringFromParam(params)
        var columns = fields.mkString(",")
        var items = parameters.split("\\|")
        // TODO: 用where 
        var sqlCmd = f"select $columns from $table order by"
        for(item <- items){
            if (item.split("=").length==2){
                var field = item.split("=")(0)
                var value = item.split("=")(1)
                value match {
                    case x_ if x_ == "1" => sqlCmd += f" `$field` desc"
                    case x_ if x_ == "0" => sqlCmd += f" `$field`"
                    case _ =>
                }
            }
        }
        if (sqlCmd.endsWith("by")){
            sqlCmd = sqlCmd.substring(0,sqlCmd.length - 8)
        }
        topK match {
            case x_ if x_ > 0 => sqlCmd += s" limit $topK"
            case _ =>
        }
        var res = hc.sql(sqlCmd)
//        res.write.csv(Constants.SAVE_DIR+f"/$table")
        res.write.mode(SaveMode.Overwrite).format("csv").save(Constants.SAVE_DIR+f"/$table.csv")
//        res.map(user => user.getValuesMap[Any](Array("name","url"))).collect()
//        res.saveAsTextFile(Constants.SAVE_DIR+f"/$table")
    }
}
