package hu.cubix.hr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {
    public FilterConfiguration(ObjectMapper objectMapper) {
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider().setFailOnUnknownId(true);
        simpleFilterProvider.addFilter("employeeFilter", SimpleBeanPropertyFilter.serializeAll());
        objectMapper.setFilterProvider(simpleFilterProvider);
    }
}
