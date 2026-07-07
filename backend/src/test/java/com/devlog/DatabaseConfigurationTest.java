package com.devlog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.boot.env.YamlPropertySourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DatabaseConfigurationTest {

    @Autowired
    private Environment environment;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Test
    @DisplayName("default application datasource uses MySQL with environment variable overrides")
    void defaultDatasourceUsesMySqlWithEnvironmentOverrides() throws IOException {
        PropertySource<?> applicationYaml = loadApplicationYaml();

        assertThat(applicationYaml.getProperty("spring.datasource.url"))
                .isEqualTo("${DEVLOG_DB_URL:jdbc:mysql://localhost:3306/devlog?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8}");
        assertThat(applicationYaml.getProperty("spring.datasource.driver-class-name"))
                .isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(applicationYaml.getProperty("spring.datasource.username"))
                .isEqualTo("${DEVLOG_DB_USERNAME:root}");
        assertThat(applicationYaml.getProperty("spring.datasource.password"))
                .isEqualTo("${DEVLOG_DB_PASSWORD:}");
    }

    @Test
    @DisplayName("gradle test task uses the test profile with H2")
    void gradleTestTaskUsesTestProfileWithH2() {
        assertThat(environment.getActiveProfiles()).contains("test");
        assertThat(dataSourceProperties.getUrl())
                .isEqualTo("jdbc:h2:mem:devlog-test;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");
        assertThat(dataSourceProperties.getDriverClassName()).isEqualTo("org.h2.Driver");
        assertThat(dataSourceProperties.getUsername()).isEqualTo("sa");
    }

    @Test
    @DisplayName("local application.properties is ignored and documented by an example file")
    void localApplicationPropertiesIsIgnoredAndDocumentedByExample() throws IOException {
        Path backendDir = Path.of(System.getProperty("user.dir"));
        Path rootDir = backendDir.getParent();

        assertThat(Files.readString(rootDir.resolve(".gitignore")))
                .contains("backend/application.properties")
                .contains("!backend/application.properties.example");
        assertThat(Files.readString(backendDir.resolve("application.properties.example")))
                .contains("spring.config.activate.on-profile=!test")
                .contains("spring.datasource.username=root")
                .contains("spring.datasource.password=your_mysql_password");
    }

    private PropertySource<?> loadApplicationYaml() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        ClassPathResource resource = new ClassPathResource("application.yml");
        return loader.load("application", resource).get(0);
    }
}
