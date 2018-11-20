/*
 * Config.java
 *
 * created at 2018-08-22 by d.galabov <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package bis.debug.mode.ui;


import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class Config
{
    private Properties configFile;


    public Config()
    {
        configFile = new Properties();

        String propFileName = "./config.properties";
        InputStream in = null;
        BufferedInputStream bis = null;

        try
        {

            File propFile = new File(propFileName);

            if (propFile.exists() && propFileName != null)
            {
                in = new FileInputStream(propFile);
                bis = new BufferedInputStream(in);
                configFile.load(bis);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            close(bis);
            close(in);
        }

    }


    public String getProperty(String key)
    {
        String value = this.configFile.getProperty(key);
        if (value != null)
        {
            return value;
        }

        return null;

    }


    private void close(Closeable out)
    {
        if (out != null)
        {
            try
            {
                out.close();
            }
            catch (Exception e)
            {
                // NOOP
            }
        }
    }

}
