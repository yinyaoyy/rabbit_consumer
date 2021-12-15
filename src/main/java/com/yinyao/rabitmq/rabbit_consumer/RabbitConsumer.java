package com.yinyao.rabitmq.rabbit_consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitConsumer {

    @RabbitListener(queues = "f_queue")
    public void ListenerQueue(Message message){
        System.out.println(new String(message.getBody()));
        System.out.println(message);
    }
    @RabbitListener(queues = "f_queue1")
    public void ListenerQueue1(Message message){
        System.out.println(new String(message.getBody()));
        System.out.println(message);
    }
    @RabbitListener(queues = "test_ack")
    public void testAck(Message message, Channel channel) throws IOException {
///1、获取消息的id
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //2、获取消息
            System.out.println("message:"+new String(message.getBody()));
            //3、进行业务处理
            System.out.println("=====进行业务处理====");
            //模拟出现异常
//            int  i = 5/0;
            //4、采用手动ack，一条条的消费 // 消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息（成功消费，消息从队列中删除 ）
//           是否批量.true:将一次性ack所有小于deliveryTag的消息。
            //false只确认签收当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //拒绝签收
             /*
            第三个参数：requeue：重回队列。如果设置为true，则消息重新回到queue，broker会重新发送该消息给消费端
             */
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 测试延迟死信队列
     * @param message
     */
    @RabbitListener(queues = "receive_queue")
    public void receive_queue(Message message,Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //2、获取消息
            System.out.println("message:"+new String(message.getBody()));
            //3、进行业务处理
            System.out.println("=====进行业务处理====");
            //模拟出现异常
//            int  i = 5/0;
            //4、采用手动ack，一条条的消费 // 消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息（成功消费，消息从队列中删除 ）
//           是否批量.true:将一次性ack所有小于deliveryTag的消息。
            //false只确认签收当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //拒绝签收
             /*
            第三个参数：requeue：重回队列。如果设置为true，则消息重新回到queue，broker会重新发送该消息给消费端
             */
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
