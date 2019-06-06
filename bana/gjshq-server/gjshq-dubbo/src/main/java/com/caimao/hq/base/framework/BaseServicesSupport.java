package com.caimao.hq.base.framework;

import com.caimao.bana.api.base.framework.BaseMapper;
import com.caimao.bana.api.base.framework.BaseServices;
import com.caimao.bana.api.base.page.ListRange;
import com.caimao.bana.api.base.page.Page;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseServicesSupport<T extends Serializable, PK>
		implements BaseServices<T, PK> {
	protected static Logger logger = null;
	private BaseMapper<T, PK> baseMapper;

	public BaseServicesSupport() {
	}

	public BaseServicesSupport(BaseMapper<T, PK> e, Class<?> classz) {
		this.baseMapper = e;
		logger = Logger.getLogger(classz);
	}

	public int deleteByPrimaryKey(PK id) {
		// TODO Auto-generated method stub

		return baseMapper.deleteByPrimaryKey(id);
	}

	public int insert(T t) {
		// TODO Auto-generated method stub
		return baseMapper.insert(t);
	}

	public int insertSelective(T t) {
		// TODO Auto-generated method stub
		return baseMapper.insertSelective(t);
	}

	public T selectByPrimaryKey(PK id) {
		// TODO Auto-generated method stub
		return (T) baseMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(T t) {
		// TODO Auto-generated method stub
		return baseMapper.updateByPrimaryKeySelective(t);
	}

	public int updateByPrimaryKey(T t) {
		// TODO Auto-generated method stub
		return baseMapper.updateByPrimaryKey(t);
	}

	public List<T> selectPageList(Map map) {
		// TODO Auto-generated method stub
		return baseMapper.selectPageList(map);
	}

	public List<T> selectPageList(Map map, Page page) {
		// TODO Auto-generated method stub
		map.put("page", page);
		return this.selectPageList(map);
	}

	public List<T> selectList(Map map) {
		// TODO Auto-generated method stub
		return baseMapper.selectList(map);
	}

	public ListRange<T> selectRangePageList(Map map, Page page) {
		// TODO Auto-generated method stub
		ListRange<T> listRange = new ListRange<T>();
		page.setCount(this.selectCount(map));
		map.put("page", page);
		listRange.setPage(page);
		listRange.setData(this.selectPageList(map));
		logger.debug("listRange:" + listRange);
		return listRange;
	}

	public int selectCount(Map map) {
		return baseMapper.selectCount(map);
	}

}
