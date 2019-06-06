/*
*JsonBigDecimalSerializer.java
*Created on 2015/1/9 11:20
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.gate.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author 闫继钢
 * @version 1.0.1
 */
public class JsonBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private static final Logger logger = LoggerFactory.getLogger(JsonBigDecimalSerializer.class);

    @Override
    public void serialize(BigDecimal value, JsonGenerator jsgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        logger.debug("BigDecimal value {} will be serialized to {}", value, value.toPlainString());
        jsgen.writeString(value.toPlainString());
    }

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }
}
