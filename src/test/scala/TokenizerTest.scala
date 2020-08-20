import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.SparkSession

object TokenizerTest {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .appName("sfs")
            .master("local[2]")
            .getOrCreate()

        val training = spark.createDataFrame(Seq(
            (0L, "a b c d e spark", 1.0),
            (1L, "b d", 0.0),
            (2L, "spark f g h", 1.0),
            (3L, "hadoop mapreduce", 0.0),
            (4L, "b spark who", 1.0),
            (5L, "g d a y", 0.0),
            (6L, "spark fly", 1.0),
            (7L, "was mapreduce", 0.0),
            (8L, "e spark program", 1.0),
            (9L, "a e c l", 0.0),
            (10L, "spark compile", 1.0),
            (11L, "hadoop software", 0.0)
        )).toDF("id", "text", "label")

        // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.
//        val tokenizer = new Tokenizer()
//            .setInputCol("text")
//            .setOutputCol("words")
//        val hashingTF = new HashingTF()
//            .setInputCol(tokenizer.getOutputCol)
//            .setOutputCol("features")
//        training.cache()
//        val tokenizerData = tokenizer.transform(training)
//        tokenizerData.select("words").collect().foreach(println)
//        println("\n\n")
//        val hashData = hashingTF.transform(tokenizerData)
//        hashData.select("features").collect().foreach(println)
    }
}
