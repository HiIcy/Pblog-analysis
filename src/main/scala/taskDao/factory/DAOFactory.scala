package taskDao.factory

import taskDao.{TopkArticleByConditionDao, TopkUserByConditionDao, UserArticleWithConditionDao, UserEstimateDao}
import taskImpl._

object DAOFactory {
    def getTopkArticleByConditionDao:TopkArticleByConditionDao=new TopkArticleByConditionImpl
    def getTopkUserByConditionDao:TopkUserByConditionDao=new TopkUserByConditionImpl
    def getUserArticleWithConditionDao:UserArticleWithConditionDao=new UserArticleWithConditionImpl
    def getUserEstimateDao:UserEstimateDao = new UserEstimateImpl
}
