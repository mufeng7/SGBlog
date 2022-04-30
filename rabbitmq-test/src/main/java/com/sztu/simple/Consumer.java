package com.sztu.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

public class Consumer {
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
            channel.basicConsume("queue1", true, new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    System.out.println("收到消息是：" + new String(delivery.getBody(), "UTF-8"));
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println("接收消息失败！");
                }
            });
            System.out.println("开始接收消息！");
            System.in.read();

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
