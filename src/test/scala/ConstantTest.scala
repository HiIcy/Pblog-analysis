import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

object ConstantTest {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .appName("sfs")
            .master("local[2]")
            .getOrCreate()
        import spark.implicits._
        val df = Seq(
            (2,1),
            (3,2),
            (4,4),
            (2,3),
            (2,5),
            (4,1),
            (2,9),
            (4,10),
            (2,2)
        ).toDF("grade","dd")
        var count = df.select("grade").rdd
            .map(x => (x,1))
            .reduceByKey(_+_)
            .collect()

        count.foreach{
            x => println(s"didi:${x._1}: ${x._2}")
        }
        var counts = df.select("grade")
            .groupBy(col("grade"))
            .count().collect()
        counts.foreach{
            row => println(s"kk: ${row.get(0)}: ${row.get(1)}")
        }
    }
}