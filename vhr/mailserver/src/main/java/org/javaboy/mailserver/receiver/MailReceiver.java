package org.javaboy.mailserver.receiver;

import com.rabbitmq.client.Channel;
import org.javaboy.vhr.web.model.Employee;
import org.javaboy.vhr.web.model.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 邮件发送过程
 */
@Component
public class MailReceiver {

    public static final Logger logger = LoggerFactory.getLogger(MailReceiver.class);

    // 这个就是邮件发送的实现类
    @Resource
    JavaMailSender javaMailSender;

    @Resource
    MailProperties mailProperties;

    // 推荐在 Spring Boot 中使用 Thymeleaf 来构建邮件模板。因为 Thymeleaf 的自动化配置提供了一个 TemplateEngine，
    // 通过 TemplateEngine可以方便的将 Thymeleaf 模板渲染为 HTML ，同时，Thymeleaf 的自动化配置在这里是继续有效的
    @Resource
    TemplateEngine templateEngine;

    @Resource
    StringRedisTemplate stringredisTemplate;

    // 开启手动确认，先关闭自动确认spring.rabbitmq.listener.simple.acknowledge-mode=manual
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (stringredisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            logger.info(msgId + ":消息已经被消费");
            channel.basicAck(tag, false);//确认消息已消费
            return;
        }
        //收到消息，发送邮件，javaMailSender获取一个复杂邮件对象MimeMessage
        MimeMessage msg = javaMailSender.createMimeMessage();
        // 发送邮箱的帮助类
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(employee.getEmail());
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("入职欢迎");
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJobLevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            // 发送了邮件，前面的helper仅仅是帮助设置
            javaMailSender.send(msg);
            stringredisTemplate.opsForHash().put("mail_log", msgId, "mail_ack");
            channel.basicAck(tag, false);
            logger.info(msgId + ":邮件发送成功");
        } catch (MessagingException e) {
            // basicNack：这个是告诉 RabbitMQ 当前消息未被成功消费，该方法有三个参数：
            // 第一个参数表示消息的 id；
            // 第二个参数 multiple
            // 如果为 false，表示仅拒绝当前消息的消费
            // 如果为 true，则表示拒绝当前消息之前所有未被当前消费者确认的消息；
            // 第三个参数 requeue 含义和前面所说的一样，被拒绝的消息是否重新入队
            channel.basicNack(tag, false, true);
            e.printStackTrace();
            logger.error("邮件发送失败：" + e.getMessage());
        }
        // 消费者在消费完一条消息后，向 RabbitMQ 发送一个 ack 确认，
        // 此时由于网络断开或者其他原因导致 RabbitMQ 并没有收到这个 ack，
        // 那么此时 RabbitMQ 并不会将该条消息删除，当重新建立起连接后，消费者还是会再次收到该条消息，这就造成了消息的重复消费。
        // 同时，由于类似的原因，消息在发送的时候，同一条消息也可能会发送两次（参见四种策略确保 RabbitMQ 消息发送可靠性！你用哪种？）。
        // 种种原因导致我们在消费消息时，一定要处理好幂等性问题。
    }
}
