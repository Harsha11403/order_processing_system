package com.precize.project.order_processing_system.ingest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precize.project.order_processing_system.events.Event;

public class EventReader {

	private final ObjectMapper mapper;
	
	public EventReader(ObjectMapper mapper)
	{
		this.mapper = mapper;
	}
	
	public void processFile(Path path, Consumer<Event> eventConsumer)
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
			String line;
			int lineNo = 0;
			while((line = br.readLine()) != null)
			{
				lineNo++;
				String trimmed = line.trim();
				if(trimmed.isEmpty())
				{
					continue;
				}
				try {
					Event event = mapper.readValue(trimmed, Event.class);
					if(event == null)
					{
						System.err.println("Parsed null event at line: " + lineNo);
						continue;
					}
					eventConsumer.accept(event);
				}
				catch(Exception e)
				{
					System.err.println("Failed to parse the line: " + lineNo + ":" + e.getMessage());
				}
			}
		}
		catch(IOException e)
		{
			System.err.println("ERROR: Unable to read file: " + e.getMessage());
		}
	}
}
