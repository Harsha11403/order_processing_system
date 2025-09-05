package com.precize.project.order_processing_system;

import java.nio.file.Path;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.precize.project.order_processing_system.ingest.EventReader;
import com.precize.project.order_processing_system.observe.AlertObserver;
import com.precize.project.order_processing_system.observe.LoggerObserver;
import com.precize.project.order_processing_system.process.EventProcessor;
import com.precize.project.order_processing_system.repo.OrderRepository;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String path = args.length > 0 ? args[0] : "src/main/resources/events.ndjson";
        System.out.println("Starting Order Processor. Reading: " + path);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OrderRepository repository = new OrderRepository();
        EventProcessor processor = new EventProcessor(repository, mapper);
        processor.registerObserver(new LoggerObserver());
        processor.registerObserver(new AlertObserver());

        EventReader reader = new EventReader(mapper);
        reader.processFile(Path.of(path), processor::processEvent);

        System.out.println("Processing complete. Final orders:");
        repository.findAll().forEach((o) -> System.out.println(o.getOrderId()));    
    }
}
