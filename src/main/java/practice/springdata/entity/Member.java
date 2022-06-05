package practice.springdata.entity;

import lombok.Getter;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ext.JodaDeserializers;
import org.codehaus.jackson.map.ext.JodaSerializers;

import java.time.LocalDateTime;

@Getter
public class Member {

    // 회원 번호
    public int memIdx;

    // 활동 이력 날짜
    @JsonSerialize(using = JodaSerializers.LocalDateSerializer.class)
    @JsonDeserialize(using = JodaDeserializers.LocalDateTimeDeserializer.class)
    public LocalDateTime logDateTime;

    public Member() {
        this.memIdx = 0;
        this.logDateTime = LocalDateTime.now();
    }

    public Member(int memIdx) {
        this.memIdx = memIdx;
        this.logDateTime = LocalDateTime.now();
    }

    public int getMemIdx() {
        return this.memIdx;
    }
}
