package com.xmcc.dao.Impl;

import com.xmcc.dao.BatchDao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {


    @PersistenceContext
    private EntityManager em;//批量处理管理器

    @Override
    public void batchInsert(List<T> list) {

        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if ( ( i+1 ) % 50 ==0 || i == list.size()-1 ){ //每50条执行一次写入数据库操作
                em.flush();
                em.clear();
            }
        }

    }

}
