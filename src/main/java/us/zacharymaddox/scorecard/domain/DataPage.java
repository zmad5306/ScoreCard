package us.zacharymaddox.scorecard.domain;

import java.util.Collection;

public class DataPage<T> {
	
	private Collection<T> data;
	private Integer rows;
	private Integer page;
	private Long count;
	
	protected DataPage() {
		super();
	}
	
	public DataPage(Collection<T> data, Integer rows, Integer page, Long count) {
		this.data = data;
		this.rows = rows;
		this.page = page;
		this.count = count;
	}

	public Collection<T> getData() {
		return data;
	}
	public void setData(Collection<T> data) {
		this.data = data;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getPages() {
		if (count < rows) {
			return 1L;
		} else if (count % rows == 0) {
			return count / rows;
		} else {
			return (count / rows) + 1;				
		}
	}
	
}
