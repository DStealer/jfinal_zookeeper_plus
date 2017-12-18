package com.kuark.jfzk.demo.config;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.kuark.jfzk.demo.TestBase;
import org.junit.Test;

public class DaoCreator extends TestBase {
    public static final String BathPath = PathKit.getWebRootPath() + "/src/main/java/";

    @Test
    public void tt01() throws Exception {
        Generator generator = new Generator(DbKit.getConfig().getDataSource(),
                "com.kuark.jfzk.demo.model.base",
                BathPath + "com/kuark/jfzk/demo/model/base",
                "com.kuark.jfzk.demo.model",
                BathPath + "com/kuark/jfzk/demo/model");
        generator.setDialect(new MysqlDialect());
        generator.generate();
    }
}
