package com.central.canal.consts;


/**
 * @Description 通用枚举类
 * @author weicl
 * @date 2021年3月12日
 */
public class CommonEnums {
	
	/**
	 * canal同步数据库操作
	 */
	public enum CanalOprDBType{
		
		INSERT("新增","INSERT"),
		DELETE("删除","DELETE"),
		UPDATE("更新","UPDATE");
		private String value;
		private String name;
		private CanalOprDBType(String name,String value){
			this.value = value;
			this.name = name;
		}
		public String getValue(){
			return this.value;
		}
		public String getName(){
			return this.name;
		}
	}
	
	
}
