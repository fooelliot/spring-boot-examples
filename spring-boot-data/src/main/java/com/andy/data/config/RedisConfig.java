//package com.andy.data.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * redis配置
// *
// * @author: lyon
// * @createBy: 2018-07-19 22:36
// **/
//@Configuration
//@EnableCaching
//public class RedisConfig extends CachingConfigurerSupport {
//
//    @Bean
//    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
//                                            MessageListenerAdapter listenerAdapter) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    @Bean
//    public Receiver receiver(CountDownLatch latch) {
//        return new Receiver(latch);
//    }
//
//    @Bean
//    public CountDownLatch latch() {
//        return new CountDownLatch(1);
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
//        return new StringRedisTemplate(connectionFactory);
//    }
//
//    public class Receiver {
//
//        private CountDownLatch latch;
//
//        @Autowired
//        public Receiver(CountDownLatch latch) {
//            this.latch = latch;
//        }
//
//        public void receiveMessage(String message) {
//            latch.countDown();
//        }
//    }
//
//}