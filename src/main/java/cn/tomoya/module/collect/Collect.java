package cn.tomoya.module.collect;

import cn.tomoya.common.BaseModel;
import cn.tomoya.common.Constants.CacheEnum;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

/**
 * Created by Tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://bbs.tomoya.cn
 */
public class Collect extends BaseModel<Collect> {

    public static final Collect me = new Collect();

    /**
     * 根据话题id与用户查询收藏记录
     * @param tid
     * @param uid
     * @return
     */
    public Collect findByTidAndUid(Integer tid, Integer uid) {
        Cache cache = Redis.use();
        Collect collect = cache.get(CacheEnum.collect.name() + tid + "_" + uid);
        if(collect == null) {
            collect = super.findFirst(
                    "select * from pybbs_collect where tid = ? and uid = ?",
                    tid,
                    uid
            );
            cache.set(CacheEnum.collect.name() + tid + "_" + uid, collect);
        }
        return collect;
    }

    /**
     * 查询话题被收藏的数量
     * @param tid
     * @return
     */
    public Long countByTid(Integer tid) {
        Cache cache = Redis.use();
        Long count = cache.get(CacheEnum.collectcount.name() + tid);
        if(count == null) {
            count = super.findFirst(
                    "select count(1) as count from pybbs_collect where tid = ?",
                    tid
            ).getLong("count");
            cache.set(CacheEnum.collectcount.name() + tid, count);
        }
        return count;
    }

}
