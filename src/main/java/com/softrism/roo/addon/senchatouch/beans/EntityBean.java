package com.softrism.roo.addon.senchatouch.beans;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sudhir
 * Date: 2/4/14
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityBean
{
    private String name;
    private LinkedHashMap<String,String> attrMap;

    public EntityBean()
    {
    }

    public EntityBean(String name)
    {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getLowerCase() {
        return name.toLowerCase();
    }

    public LinkedHashMap<String, String> getAttrMap() {
        if(attrMap == null){
            attrMap = new LinkedHashMap<String, String>();
        }
        return attrMap;
    }

    public void setAttrMap(LinkedHashMap<String, String> attrMap) {
        this.attrMap = attrMap;
    }
}
