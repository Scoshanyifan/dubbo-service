package com.scosyf.dubbo.log.provider.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @program: bucks
 * @description:
 * @author: kunbu
 * @create: 2019-08-26 16:38
 **/
@Configuration
public class MongoConfig {

    /**
     * 去除spring-data-mongo自动生成的_class字段
     * https://www.cnblogs.com/keeya/p/9969535.html
     * https://blog.csdn.net/asahinokawa/article/details/83894670
     *
     *
     *
     * @param factory
     * @param context
     * @return
     * @author kunbu
     * @time 2019/8/26 16:45
     **/
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return converter;
    }

    /**
     * 配置自定义mongo中document和代码中模型的转换（参考Spring全家桶）
     *
     * @param
     * @return
     * @author kunbu
     * @time 2019/8/26 16:45
     **/
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Lists.newArrayList());
    }

}
