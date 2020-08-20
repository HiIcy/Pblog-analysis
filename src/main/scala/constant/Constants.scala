package constant

object Constants {
    /**
      * USER_INFO表的字段
     */
    val USER_TABLE_NAME = "user_info"
    val USER_ID="id"
    val USER_NAME="name"
    val USER_CREATES="creates"
    val USER_FANS="fans"
    val USER_PRAISES="praises"
    val USER_COMMENTS="comments"
    val USER_VISITS="visits"
    val USER_GRADE ="grade"
    val USER_CREDITS="credits"
    val USER_RANK="rank"
    val USER_BADGES="badges"
    val USER_URL="url"

    /**
      * ARTICLE_INFO的配置信息
      * */
    val ARTICLE_TABLE_NAME = "article_info"
    val ARTICLE_ID = "id"
    val ARTICLE_TITLE = "title"
    val ARTICLE_CREATE_TIME = "create_time"
    val ARTICLE_READ_COUNT = "read_count"
    val ARTICLE_NATURE = "nature"
    val ARTICLE_PRAISES = "praises"
    val ARTICLE_COMMENTS = "comments"
    val ARTICLE_URL = "url"
    val ARTICLE_USER_NAME = "user_name"

    /**
      * 保存位置
    * */
    val SAVE_DIR = "/data/soft/pblog"
}
