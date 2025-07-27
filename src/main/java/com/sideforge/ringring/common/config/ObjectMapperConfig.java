package com.sideforge.ringring.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean(name="objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }


    @Bean(name="strictObjectMapper")
    public ObjectMapper strictObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // JSON 문자열을 Java 객체로 변환(deserialize)할 때, 필요한 필드들을 다 처리하고도 JSON에 더 남은 데이터(token)가 있다면 예외 발생
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        // Java 클래스에 존재하지 않는 필드가 JSON에 포함되면 예외 발생
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean(name="flexibleObjectMapper")
    public ObjectMapper flexibleObjectMapper() {
        ObjectMapper objectMapper = JsonMapper.builder()
                // boolean 필드 처리 설정
                .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true)
                // 알 수 없는 속성 무시
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 빈 컬렉션을 null로 처리하지 않음
                .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)
                // 대소문자 구분 없는 속성 일치
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                // 숫자를 String으로 받을 수 있도록 허용
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                // 날짜/시간 형식 유연하게 처리
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                // private 필드에 직접 접근 허용
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                // [정밀도 보장] BigDecimal / BigInteger 타입의 JSON 숫자를 파싱할 때 기본 파서를 사용하게 설정
                .configure(JsonParser.Feature.USE_FAST_BIG_NUMBER_PARSER, false)
                // [정밀도 보장] BigDecimal 값을 scientific notation(지수 표기법) 대신 일반적인 문자열 숫자로 출력
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                // [성능 최적화] 역직렬화할 때 해당 타입에 대한 deserializer를 미리 로딩해 캐시에 저장
                .configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true)
                // [성능 최적화] double 파싱 속도 향상 (Fast Double Parser 사용)
                .configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true)
                // [성능 최적화] double 직렬화 속도 향상 (Fast Writer 사용)
                .configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true)
                .build();

        // 시간 모듈 등록
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // null 값 포함 설정
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 필드 이름 명명 전략
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        return objectMapper;
    }
}
