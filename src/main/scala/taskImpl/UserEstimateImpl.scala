package taskImpl
import constant.Constants
import taskDao.UserEstimateDao
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{LinearSVC, LogisticRegression, RandomForestClassifier}
import org.apache.spark.ml.feature.{Imputer, StandardScaler, VectorAssembler}
import org.apache.spark.ml.linalg.{Matrix, Vector}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.mutable.ListBuffer
import scala.reflect.internal.util.TableDef.Column

class UserEstimateImpl extends UserEstimateDao {
//        val userFields = Array[String]("name", "url")
//        this.TopkByCondition(hc,params)(userFields,Constants.USER_TABLE_NAME)
    override def analysis(spark: SparkSession, dt: DataFrame): Unit = {
        import spark.implicits._
        var df = dt.select("grade","creates","fans","praises","comments","visits")
//        var features = df.toDF("features")
        var coeff = ListBuffer[Double]()
        // TODO: 用VectorAssembler合并
        for (column <- df.columns) {
            var coeff1 = df.stat.corr(s"$column","grade")
            coeff.append(coeff1)
        }
        coeff.foreach{coeff1 =>
            println(s"Pearson correlation matrixs:\n $coeff1")
        }
    }

    override def fit(spark:SparkSession,dt: DataFrame): Unit = {
        import spark.implicits._
        // REW: 改变数据类型 with grade
        var df = dt.select("grade","name","creates","fans","praises","comments","visits")
            .withColumn("grade",'grade cast DoubleType)
        var count = df.select("grade").rdd.map(x => (x,1)).reduceByKey(_+_).collect()
//                .groupBy(col("grade"))
//                .sum()
        count.foreach{
            x => println(s"didi:${x._1}: ${x._2}")
        }

//        println(s"invalid label count : $count")
/**
  * spark transformer
  * */
        val schema = df.schema.map(f=>s"${f.name}").drop(2) // 扔掉前两列
        println(s"schema: $schema")
        // REW:ML的VectorAssembler是一个transformer,要求数据类型不能是string，将多列数据转化为单列的向量列，
        // 比如把age、income等等字段列合并成一个 userFea 向量列，方便后续训练concat_as
        val assembler = new VectorAssembler().setInputCols(schema.toArray).setOutputCol("assembel")
//        val data = df.na.fill("mean",schema.toArray)
        var dataset = assembler.transform(df)
        dataset.schema.foreach(println)
        //  数据标准化
        var scaler = new StandardScaler()
            .setInputCol("assembel")
            .setOutputCol(s"features")
            .setWithStd(true)
            .setWithMean(false)
        var scalerModel = scaler.fit(dataset)
        dataset = scalerModel.transform(dataset).withColumnRenamed("grade","label")

        var countss = dataset.select("label")
            .groupBy(col("label"))
            .count().collect()
        countss.foreach{
            row => println(s"ss: ${row.get(0)}: ${row.get(1)}")
        }

        //标签缺省
//        val imputer = new Imputer()
//            .setInputCols(Array("grade"))
//            .setOutputCols(Array("label"))
//            .setMissingValue(0)
//        dataset = imputer.fit(dataset).transform(dataset)


        /**
          * spark estimator
          * */
        val datasetSplit = dataset.randomSplit(Array(0.8,0.2))
        val (train,test) = (datasetSplit(0),datasetSplit(1))
        val lr = new LogisticRegression() // TODO: 使用回归
            .setMaxIter(100)
            .setRegParam(0.3)
            .setElasticNetParam(0.8)
        val lrModel = lr.fit(train)

//        val pipeline = new Pipeline()
//            .setStages(Array(scaler,imputer,lr))
        val paramGrid = new ParamGridBuilder()
            .addGrid(lr.regParam,Array(1e-1,1e-2,1e-3))
            .build()

        val trainingSummary = lrModel.summary

        // Obtain the objective per iteration
        val objectiveHistory = trainingSummary.objectiveHistory
        println("objectiveHistory:")
        objectiveHistory.foreach(println)

        // for multiclass, we can inspect metrics on a per-label basis
        println("False positive rate by label:")
        trainingSummary.falsePositiveRateByLabel.zipWithIndex.foreach { case (rate, label) =>
            println(s"label $label: $rate")
        }

        println("True positive rate by label:")
        trainingSummary.truePositiveRateByLabel.zipWithIndex.foreach { case (rate, label) =>
            println(s"label $label: $rate")
        }

        println("Precision by label:")
        trainingSummary.precisionByLabel.zipWithIndex.foreach { case (prec, label) =>
            println(s"label $label: $prec")
        }

        println("Recall by label:")
        trainingSummary.recallByLabel.zipWithIndex.foreach { case (rec, label) =>
            println(s"label $label: $rec")
        }

        println("F-measure by label:")
        trainingSummary.fMeasureByLabel.zipWithIndex.foreach { case (f, label) =>
            println(s"label $label: $f")
        }

        val accuracy = trainingSummary.accuracy
        val falsePositiveRate = trainingSummary.weightedFalsePositiveRate
        val truePositiveRate = trainingSummary.weightedTruePositiveRate
        val fMeasure = trainingSummary.weightedFMeasure
        val precision = trainingSummary.weightedPrecision
        val recall = trainingSummary.weightedRecall
        println(s"Accuracy: $accuracy\nFPR: $falsePositiveRate\nTPR: $truePositiveRate\n" +
            s"F-measure: $fMeasure\nPrecision: $precision\nRecall: $recall")

        // 交叉验证
//        val cv = new CrossValidator()
//            .setEstimator(pipeline)
//            .setEvaluator(new MulticlassClassificationEvaluator)
//            .setEstimatorParamMaps(paramGrid)
//            .setNumFolds(10)  // Use 3+ in practice
//            .setParallelism(2)
//        val cvModel = cv.fit(train)
//
//        cvModel.transform(test)
//            .select("name","probability", "prediction")
//            .collect().slice(0,50)
//            .foreach{
//                case Row(name:String,prob:Vector,pred:Double) =>
//                    println(s"($name) --> prob=$prob, prediction=$pred")
//            }
//        println(s"metrics: ${cvModel.avgMetrics}")
    }

    override def eval(spark:SparkSession,dt: DataFrame): Unit = {

    }
}
