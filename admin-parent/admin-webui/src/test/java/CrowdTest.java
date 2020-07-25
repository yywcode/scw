import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void testTX() {
        Admin admin = new Admin(null, "jack", "123456", "jack", "jack@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testLog() {
        //1.获取Logger对象,这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        //2.根据不同的日志级别打印日志
        logger.debug("Hello I am Debug level");
        logger.debug("Hello I am Debug level");
        logger.debug("Hello I am Debug level");

        logger.info("Info level!!!");
        logger.info("Info level!!!");
        logger.info("Info level!!!");

        logger.warn("Warn level!!!");
        logger.warn("Warn level!!!");
        logger.warn("Warn level!!!");

        logger.error("Error level");
        logger.error("Error level");
        logger.error("Error level");

    }


    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "fuck", "123123", "fuck", "tom@qq.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println("影响行数" + insert);

    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
