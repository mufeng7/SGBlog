package com.sztu.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) {
        /**
         * 创建连接工程
         */
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("120.24.40.87");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        connectionFactory.setVirtualHost("/");


        Connection connection=null;
        Channel channel=null;
        try {
            //创建连接connection
            connection=connectionFactory.newConnection("生产者");
            //通过连接获取通道
            channel=connection.createChannel();
            //通过创建交换机，声明队列，绑定关系，路由key，发送消息和接收消息
            String queueName="queue1";
            /**
             * @params1
             * @params1
             * @params1
             * @params1
             * @params1
             */
            channel.queueDeclare(queueName,false,false,false,null);
            String message="hello jerry";
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("=======>消息发送成功！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if (channel!=null && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null&& connection.isOpen()){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
