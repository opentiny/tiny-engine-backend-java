package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.service.app.I18nEntryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class I18nEntryServiceImpl implements I18nEntryService {

    @Autowired
    private I18nEntryMapper i18nEntryMapper;

    /**
    *  查询表t_i18n_entry所有数据
    */
    @Override
    public List<I18nEntry> findAllI18nEntry() throws ServiceException {
        return i18nEntryMapper.findAllI18nEntry();
    }

    /**
    *  根据主键id查询表t_i18n_entry信息
    *  @param id
    */
    @Override
    public I18nEntry findI18nEntryById(@Param("id") Integer id) throws ServiceException {
        return i18nEntryMapper.findI18nEntryById(id);
    }

    /**
    *  根据条件查询表t_i18n_entry数据
    *  @param i18nEntry
    */
    @Override
    public List<I18nEntry> findI18nEntryByCondition(I18nEntry i18nEntry) throws ServiceException {
        return i18nEntryMapper.findI18nEntryByCondition(i18nEntry);
    }

    /**
    *  根据主键id删除表t_i18n_entry数据
    *  @param id
    */
    @Override
    public Integer deleteI18nEntryById(@Param("id") Integer id) throws ServiceException {
        return i18nEntryMapper.deleteI18nEntryById(id);
    }

    /**
    *  根据主键id更新表t_i18n_entry数据
    *  @param i18nEntry
    */
    @Override
    public Integer updateI18nEntryById(I18nEntry i18nEntry) throws ServiceException {
        return i18nEntryMapper.updateI18nEntryById(i18nEntry);
    }

    /**
    *  新增表t_i18n_entry数据
    *  @param i18nEntry
    */
    @Override
    public Integer createI18nEntry(I18nEntry i18nEntry) throws ServiceException {
        return i18nEntryMapper.createI18nEntry(i18nEntry);
    }
}
