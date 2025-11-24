package org.example.disaster;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.util.Collector;
import org.apache.flink.streaming.api.functions.source.legacy.SourceFunction;
import java.time.Duration;


public class DisasterTweetJob {

    public static void main(String[] args) throws Exception {
        
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Checkpointing necesario para Reactive Mode
        env.enableCheckpointing(5000); 

    
        DataStream<String> rawTweets = env.addSource(new TwitterSourceSimulator())
                .name("TwitterStreamSource");

        
        DataStream<Tuple2<String, Integer>> alertStream = rawTweets
                .flatMap(new KeywordFilter())
                .name("KeywordFilter")
                .keyBy(value -> value.f0)
                .window(SlidingProcessingTimeWindows.of(
                        Duration.ofSeconds(60), 
                        Duration.ofSeconds(10))) 
                .sum(1) 
                .name("RollingAlertCounter");

        alertStream.filter(tuple -> tuple.f1 > 50) 
                .print()
                .name("AlertSink");

        env.execute("Disaster Detection - Reactive Mode");
    }

    public static class KeywordFilter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String tweet, Collector<Tuple2<String, Integer>> out) {
            String lowercaseTweet = tweet.toLowerCase();
            if (lowercaseTweet.contains("terremoto")) {
                String[] parts = lowercaseTweet.split(" ");
                String zona = "general";
                for(String part : parts) {
                    if(part.startsWith("zona-")) {
                        zona = part;
                        break;
                    }
                }
                out.collect(new Tuple2<>("terremoto-" + zona, 1));
            }
        }
    }
    
    public static class TwitterSourceSimulator implements SourceFunction<String> {
        private volatile boolean isRunning = true;
        private java.util.Random random = new java.util.Random();

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            while (isRunning) {
                int zonaId = random.nextInt(10); 
                ctx.collect("Alerta de terremoto en la zona-" + zonaId + " #sismo");
                Thread.sleep(50);
            }
        }

        @Override
        public void cancel() { isRunning = false; }
    }
}