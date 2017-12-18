package com.kuark.jfzk.demo;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import org.junit.Before;

public class TestBase {
    @Before
    public void setUp() throws Exception {
        Prop prop = PropKit.use("datasource.properties");
        DruidPlugin druidPlugin = new DruidPlugin(prop.get("jdbc.url"), prop.get("jdbc.username"),
                prop.get("jdbc.password"), prop.get("jdbc.driverClassName"));
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        druidPlugin.start();
        arp.start();
    }
}
