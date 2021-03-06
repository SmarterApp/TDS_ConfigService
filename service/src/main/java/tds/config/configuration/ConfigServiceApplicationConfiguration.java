/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.config.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import tds.common.configuration.CacheConfiguration;
import tds.common.configuration.DataSourceConfiguration;
import tds.common.configuration.EventLoggerConfiguration;
import tds.common.configuration.RedisClusterConfiguration;
import tds.common.configuration.RestTemplateConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

/**
 * This is the base configuration class for the config microservice
 */
@Configuration
@Import({
    ExceptionAdvice.class,
    RestTemplateConfiguration.class,
    DataSourceConfiguration.class,
    RedisClusterConfiguration.class,
    CacheConfiguration.class,
    SecurityConfiguration.class,
    EventLoggerConfiguration.class,
})
public class ConfigServiceApplicationConfiguration {
}
