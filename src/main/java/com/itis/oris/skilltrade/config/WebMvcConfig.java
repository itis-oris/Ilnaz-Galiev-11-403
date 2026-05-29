package com.itis.oris.skilltrade.config;

import com.itis.oris.skilltrade.converter.StringToCategoryConverter;
import com.itis.oris.skilltrade.converter.StringToSkillConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final StringToSkillConverter stringToSkillConverter;
    private final StringToCategoryConverter stringToCategoryConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToSkillConverter);
        registry.addConverter(stringToCategoryConverter);
    }
}
