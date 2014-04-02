package com.softrism.roo.addon.senchatouch.beans;

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




    
    

}
