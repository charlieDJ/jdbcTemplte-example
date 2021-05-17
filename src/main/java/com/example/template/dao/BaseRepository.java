package com.example.template.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author dj
 * @date 2021/5/11
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * 批量插入
     *
     * @param list 实体列表
     * @return 成功标识
     */
    @Modifying
    @Transactional
    boolean saveBatch(List<T> list);

}
