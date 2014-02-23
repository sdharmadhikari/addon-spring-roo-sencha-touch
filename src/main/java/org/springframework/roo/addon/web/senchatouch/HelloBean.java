package org.springframework.roo.addon.web.senchatouch;

/**
 * Created with IntelliJ IDEA.
 * User: sudhir
 * Date: 2/4/14
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloBean
{
    private String message;

    public HelloBean()
    {
    }

    public HelloBean(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }
}
