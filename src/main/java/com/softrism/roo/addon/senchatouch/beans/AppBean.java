package com.softrism.roo.addon.senchatouch.beans;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sudhir
 * Date: 2/4/14
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppBean
{
    private String name;
    private String baseUrl;
    private ArrayList<EntityBean> entityList;

    public AppBean()
    {
    }

    public AppBean(String name)
    {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

    public ArrayList<EntityBean> getEntityList() {
        return entityList;
    }

    public void setEntityList(ArrayList<EntityBean> entityList) {
        this.entityList = entityList;
    }
}
