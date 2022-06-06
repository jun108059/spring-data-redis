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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisPojoListTest {

    @Autowired
    private RedisTemplate<String, Member> redisTemplate;

    @Test
    void 레디스_테스트() {
        // given
        ListOperations<String, Member> listOperations = redisTemplate.opsForList();
        String key = "stringKey";

        String temp;
        Member member;
        // when
        for (int i = 1; i < 150; i++) {
            member = new Member(i*1000);
            listOperations.leftPush(key, member);
        }

        // then
//        Member s = listOperations.leftPop(key);
//        System.out.println("Last List Value = " + s);
        int start = 0;
        List<Member> range = listOperations.range(key, start, 100);

        // LinkedHashMap Error Fix
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        List<Member> convertList = objectMapper.convertValue(range,
                new TypeReference<>() {});

        System.out.println("range1 = " + range.size());
        System.out.println("convert List = " + convertList.size());
        assertThat(range.size()).isEqualTo(101);
        assertThat(convertList.size()).isEqualTo(101);

        for (Member s1 : convertList) {
            System.out.println("s1 = " + s1.getMemIdx());
        }
        System.out.println("=======================================");
        range = listOperations.range(key, 100, 200);
        convertList = objectMapper.convertValue(range,
                new TypeReference<>() {});

        System.out.println("range2 = " + convertList.size());
        int index = 0;
        for (Member s2 : convertList) {
            System.out.println(" s2 = " + s2.getMemIdx() + " index : " + index++);
        }

    }
}
