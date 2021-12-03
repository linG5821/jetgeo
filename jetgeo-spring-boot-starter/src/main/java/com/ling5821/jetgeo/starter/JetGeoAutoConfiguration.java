package com.ling5821.jetgeo.starter;

import com.ling5821.jetgeo.JetGeo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsj
 * @date 2021/11/24 15:08
 */
@Configuration
@EnableConfigurationProperties(JetGeoProperties.class)
public class JetGeoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JetGeo jetGeo(JetGeoProperties properties) {
        com.ling5821.jetgeo.config.JetGeoProperties config = new com.ling5821.jetgeo.config.JetGeoProperties();
        config.setGeoDataParentPath(properties.getGeoDataParentPath());
        config.setLevel(properties.getLevel());
        config.setS2MinLevel(properties.getS2MinLevel());
        config.setS2MaxLevel(properties.getS2MaxLevel());
        config.setS2MaxCells(properties.getS2MaxCells());
        config.setLoadCacheInitialCapacity(properties.getLoadCacheInitialCapacity());
        config.setLoadCacheMaximumSize(properties.getLoadCacheMaximumSize());
        config.setLoadCacheExpireAfterAccess(properties.getLoadCacheExpireAfterAccess());
        config.setLoadCacheRefreshAfterWrite(properties.getLoadCacheRefreshAfterWrite());
        return new JetGeo(config);
    }

}
