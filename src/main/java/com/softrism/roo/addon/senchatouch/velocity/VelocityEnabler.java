package com.softrism.roo.addon.senchatouch.velocity;

import com.softrism.roo.addon.senchatouch.beans.AppBean;
import com.softrism.roo.addon.senchatouch.beans.EntityBean;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: sudhir
 * Date: 3/4/14
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class VelocityEnabler {

    private VelocityEngine velocityEngine;

    private VelocityContext velocityContext;

    public VelocityEnabler() {
        System.out.println("Enabling velocity..");

        Properties properties = new Properties();
        try {
            properties.load( getClass().getClassLoader().getResourceAsStream( "velocity.properties" ) );
        } catch (IOException e) {
            System.out.println("Velocity properties not found !!");
        }
        System.out.println("Enabled velocity..");
        // Create and initialize the template engine
        try{
            velocityEngine = new VelocityEngine( properties );
        }catch(Exception e){
            System.out.println("Velocity engine initiation problem");
            e.printStackTrace();
        }

        velocityContext = new VelocityContext();

    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public String velocityExecute(String templateLocation, AppBean appBean, EntityBean entityBean)
    {
      try
        {
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("app", appBean);
            velocityContext.put( "entity", entityBean );

            StringWriter writer = new StringWriter();

            InputStream is = VelocityEnabler.class.getClassLoader().getResourceAsStream( templateLocation );
            System.out.println("inputstream " + is);
            String inputString = IOUtils.toString(is, "UTF-8");

            velocityEngine.evaluate( velocityContext, writer, entityBean.getName() + "-log", inputString );

            return writer.toString();


        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

}
