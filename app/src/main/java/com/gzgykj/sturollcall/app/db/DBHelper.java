package com.gzgykj.sturollcall.app.db;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :数据库操作
 */
public interface DBHelper {
    /**
     * 例如增 删 改  查  等
     */
    boolean queryNewsId(int id);
    void closeDB();
    int db_insert();
    int db_delete();
    int db_update();
    int db_select();
}
