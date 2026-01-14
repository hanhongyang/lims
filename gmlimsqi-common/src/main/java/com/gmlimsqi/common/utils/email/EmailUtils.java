package com.gmlimsqi.common.utils.email;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 邮件发送工具类（支持文本/HTML正文 + 多附件，兼容PDF、图片、文档等）
 */
public class EmailUtils {

    /**
     * 简化版发送方法（推荐）：自动匹配发件人邮箱的SMTP配置
     *
     * @param fromEmail    发件人邮箱（如：xxx@qq.com）
     * @param authCode     发件人授权码（非登录密码）
     * @param toEmails     收件人邮箱数组（支持多个，如：{"a@163.com", "b@gmail.com"}）
     * @param subject      邮件主题（支持中文）
     * @param content      邮件正文内容
     * @param isHtml       正文是否为HTML格式（true=HTML，false=纯文本）
     * @param attachments  附件绝对路径数组（可选，如：{"D:/test.pdf", "C:/img.jpg"}）
     * @throws MessagingException 邮件发送异常（服务器连接、认证失败等）
     * @throws IOException        附件读取异常（文件不存在、无权限等）
     */
    public static void sendEmail(
            String fromEmail, String authCode, String[] toEmails,
            String subject, String content, boolean isHtml, MultipartFile[] attachments)
            throws MessagingException, IOException {

        // 自动匹配发件人邮箱对应的SMTP配置
        EmailSmtpConfig smtpConfig = EmailSmtpConfig.getByEmailSuffix(fromEmail);
        // 调用核心发送方法
        sendEmailWithAttachment(
                smtpConfig.getSmtpHost(),
                smtpConfig.getSmtpPort(),
                fromEmail,
                authCode,
                toEmails,
                subject,
                content,
                isHtml,
                attachments,
                smtpConfig.isSSL()
        );
    }


    /**
     * 高级发送方法：手动指定SMTP配置（用于未收录的邮箱）
     *
     * @param smtpHost     SMTP服务器地址（如：smtp.qq.com）
     * @param smtpPort     SMTP服务器端口（如：465）
     * @param fromEmail    发件人邮箱
     * @param authCode     发件人授权码
     * @param toEmails     收件人邮箱数组
     * @param subject      邮件主题
     * @param content      邮件正文
     * @param isHtml       是否为HTML格式
     * @param attachments  附件路径数组
     * @param isSSL        是否启用SSL加密
     * @throws MessagingException 邮件发送异常
     * @throws IOException        附件读取异常
     */
    public static void sendEmailWithAttachment(
            String smtpHost, int smtpPort, String fromEmail, String authCode,
            String[] toEmails, String subject, String content, boolean isHtml,
            MultipartFile[] attachments, boolean isSSL) throws MessagingException, IOException {
// ------------------ 调试：核对密码 ------------------
        System.out.println("====== 正在尝试发送邮件 ======");
        System.out.println("服务器: " + smtpHost + ":" + smtpPort);
        System.out.println("账号: " + fromEmail);
        System.out.println("密码(authCode): " + authCode);
        // 请在控制台确认打印出来的密码，是否和你现在登录电脑的密码完全一致！
        // --------------------------------------------------
        // 1. 配置SMTP服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);       // SMTP服务器地址
        props.put("mail.smtp.port", smtpPort);       // 端口
        props.put("mail.smtp.auth", "true");         // 启用身份认证
        // 加密配置（SSL或TLS）
        props.put(isSSL ? "mail.smtp.ssl.enable" : "mail.smtp.starttls.enable", "true");

        // 2. 创建带认证的会话（发件人身份验证）
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, authCode);
            }
        });
        session.setDebug(false); // 调试模式：true时打印发送日志（开发时启用）

        // 3. 构建邮件消息
        MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(fromEmail));
        // 设置收件人（支持多个，类型为TO：直接收件人）
        InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
        for (int i = 0; i < toEmails.length; i++) {
            toAddresses[i] = new InternetAddress(toEmails[i]);
        }
        message.setRecipients(Message.RecipientType.TO, toAddresses);
        // 设置主题（支持中文）
        message.setSubject(subject, "UTF-8");

        // 4. 构建邮件内容（正文 + 附件）
        MimeMultipart multipart = new MimeMultipart();

        // 4.1 添加正文部分
        MimeBodyPart contentPart = new MimeBodyPart();
        String contentType = isHtml ? "text/html;charset=UTF-8" : "text/plain;charset=UTF-8";
        contentPart.setContent(content, contentType);
        multipart.addBodyPart(contentPart);

        // 4.2 添加附件（核心修改：处理前端上传的MultipartFile）
        if (attachments != null && attachments.length > 0) {
            for (MultipartFile file : attachments) {
                // 校验前端上传的文件是否为空
                if (file.isEmpty()) {
                    throw new IOException("上传的附件不能为空");
                }

                MimeBodyPart attachmentPart = new MimeBodyPart();
                // 读取前端文件的输入流（替代本地文件流）
                try (InputStream fis = file.getInputStream()) { // 使用MultipartFile的输入流
                    // 获取文件MIME类型（优先用文件的contentType，其次自动探测）
                    String fileContentType = file.getContentType();
                    if (fileContentType == null || fileContentType.isEmpty()) {
                        // 若前端未传类型，通过文件名探测（需文件名带后缀）
                        String originalFilename = file.getOriginalFilename();
                        if (originalFilename != null) {
                            fileContentType = Files.probeContentType(Paths.get(originalFilename));
                        }
                    }
                    // 最终无法识别时用通用二进制类型
                    if (fileContentType == null) {
                        fileContentType = "application/octet-stream";
                    }

                    // 设置附件数据源（使用前端文件流）
                    DataSource dataSource = new ByteArrayDataSource(fis, fileContentType);
                    attachmentPart.setDataHandler(new DataHandler(dataSource));

                    // 处理中文文件名（用前端传来的原始文件名）
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null) {
                        originalFilename = "unknown_file";
                    }
                    String fileName = MimeUtility.encodeText(originalFilename, "UTF-8", "B");
                    attachmentPart.setFileName(fileName);
                }
                multipart.addBodyPart(attachmentPart);
            }
        }

        // 5. 设置邮件内容并发送
        message.setContent(multipart);
        Transport.send(message);
    }


    // ------------------------------ 测试示例 ------------------------------
    public static void main(String[] args) {
        try {
            // 1. 配置发送参数（替换为你的实际信息）
            String fromEmail = "your-qq@qq.com"; // 发件人邮箱（如：123456@qq.com）
            String authCode = "your-auth-code";  // 发件人授权码（需自行获取）
            // 多收件人（支持不同邮箱类型）
            String[] toEmails = {"recipient1@163.com", "recipient2@gmail.com"};
            String subject = "测试邮件（多收件人+PDF附件）"; // 邮件主题
            // 邮件正文（HTML格式）
            String content = "<h2>这是一封测试邮件</h2>" +
                    "<p>收件人：多个不同邮箱</p>" +
                    "<p>附件：包含PDF文件</p>";
            // 附件路径（替换为你的实际文件绝对路径）
            MultipartFile[] attachments = null;

            // 2. 发送邮件（自动匹配SMTP配置）
            sendEmail(fromEmail, authCode, toEmails, subject, content, true, attachments);
            System.out.println("邮件发送成功！");

        } catch (MessagingException e) {
            System.err.println("发送失败（邮件服务问题）：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("发送失败（附件问题）：" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("参数错误：" + e.getMessage());
        }
    }
}