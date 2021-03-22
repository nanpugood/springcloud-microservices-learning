package com.central.canal.page;

import java.util.List;

@SuppressWarnings("serial")
public class Page<T> extends SimplePage implements java.io.Serializable,Paginable {
	public Page() {
	}

	public Page(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page(int pageNo, int pageSize, int totalCount, List list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 当前页的数据
	 */
	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
