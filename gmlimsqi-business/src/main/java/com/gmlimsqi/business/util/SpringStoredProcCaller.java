package com.gmlimsqi.business.util;

import com.gmlimsqi.business.ranch.dto.StoredProcedureVO;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

// 需在Spring配置类中注入DataSource（示例：Spring Boot自动配置）
@Configuration
public class SpringStoredProcCaller {
    private final JdbcTemplate jdbcTemplate;

    // 构造器注入DataSource（Spring Boot会自动配置DataSource）
    public SpringStoredProcCaller(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Spring环境调用存储过程
     * @param ctype 不合格类型
     * @param keyid 关联主键ID
     * @return 结果DTO
     */
    public StoredProcedureVO callProTransCreateUnquality(String ctype, String keyid) {
        String procSql = "{call Pro_Trans_CreateUnquality(?, ?)}";
        
        // 调用存储过程并映射结果集
        return jdbcTemplate.queryForObject(procSql, new Object[]{ctype, keyid}, new RowMapper<StoredProcedureVO>() {
            @Override
            public StoredProcedureVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                StoredProcedureVO result = new StoredProcedureVO();
                result.setCode(rs.getString("code"));
                result.setMsg(rs.getString("msg"));
                return result;
            }
        });
    }
}

// Spring Boot配置示例（application.yml）
/*
spring:
  datasource:
    url: jdbc:mysql://你的数据库IP:3306/数据库名?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: 你的数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver
*/