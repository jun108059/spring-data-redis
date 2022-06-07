package practice.springdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import practice.springdata.entity.Member;

import java.util.List;

@SpringBootTest
public class RedisPojoListTest {

    @Autowired
    private RedisTemplate<String, Member> redisTemplate;

    @Test
    void 레디스_테스트() {
        // given
        ListOperations<String, Member> listOperations = redisTemplate.opsForList();
        String key = "stringKey";

        Member member;

        // when
        for (int i = 1; i < 150; i++) {
            member = new Member(i*1000);
            listOperations.leftPush(key, member);
        }

        // then
        Long maxIndex = listOperations.size(key);
        System.out.println("maxIndex = " + maxIndex);
        int index = 1;
        int start = 0;
        int end = 100;
        int SIZE = 100;
        // LinkedHashMap Error Fix
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        List<Member> range;
        List<Member> convertList;
        while (start <= maxIndex) {
            range = listOperations.range(key, start, end);
            convertList = objectMapper.convertValue(range, new TypeReference<>() {});
            System.out.println("index : " + index++ + " range = " + range.size());
            System.out.println("convert List size = " + convertList.size());
            for (Member memIdx : convertList) {
                System.out.println("memIdx = " + memIdx.getMemIdx());
            }
            System.out.println("=======================================");
            start += SIZE;
            end += SIZE;
            System.out.println("start = " + start);
        }
    }
}
