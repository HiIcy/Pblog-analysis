package utils
import constant.Constants
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSON
object ParamUtil {
    def getTaskCategoryFromArgs(args:Array[String]):Option[Int]={
        try {
            if (args != null && args.length > 0){
                return Some(args(0).toInt)
            }
        }catch {
            case e:Throwable => None
        }
        None
    }
    def parseArgs(args:Array[String]):Option[String]={
        try{
            if (args!=null && args.length > 1){
                return Some(args(1))
            }
        }catch {
            case e:Throwable => None
        }
        None
    }
    def getParamFromField(jsonObject:JSONObject,field:String):Option[String]={
        val value = jsonObject.getString(field)
        Option(value)
    }
    def getConcatStringFromParam(params:JSONObject):String={
        var keys = Constants.getClass.getFields
        var fields = keys.map{ key =>
            var ke = key.toString
            ke = ke.split("\\.").lastOption.getOrElse("")
            ke
        }
        fields = fields.filter(p => p.startsWith("USER")||p.startsWith("ARTICLE"))
        var parameters = ""
        for(field <- fields){
            parameters+=f"${getMatchfield(field,getParamFromField(params,field))}"
        }
        if (parameters.endsWith("\\|")) {
            parameters = parameters.substring(0,parameters.length - 1)
        }
        parameters
    }
    def getMatchfield(key: String, x: Option[String]): String = x match {
        case Some(y) => f"$key=$y|"
        case None => ""
    }

}
