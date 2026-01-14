package com.gmlimsqi;

import com.luciad.imageio.webp.WebPImageReaderSpi;
import com.luciad.imageio.webp.WebPImageWriterSpi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import java.util.Arrays;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }

    // ---------------------- 添加这个方法 ----------------------
    @PostConstruct
    public void initImageIOPlugins() {
        // 注册 WebP 格式插件（确保依赖生效）
        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new WebPImageReaderSpi());
        registry.registerServiceProvider(new WebPImageWriterSpi());

        // 启动时打印支持的图片格式（方便排查，可选删除）
        String[] supportedFormats = ImageIO.getReaderFormatNames();
        System.out.println("ImageIO 支持的图片格式：" + Arrays.toString(supportedFormats));
    }

}
