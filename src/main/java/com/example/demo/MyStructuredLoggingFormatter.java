package com.example.demo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.boot.json.JsonWriter;
import org.springframework.boot.logging.structured.StructuredLogFormatter;

class MyStructuredLoggingFormatter implements StructuredLogFormatter<ILoggingEvent> {

//  @Override
//  public String format(ILoggingEvent event) {
//    return "time=" + event.getTimeStamp() + " level=" + event.getLevel() + " message=" + event.getMessage() + "\n";
//  }

	private final JsonWriter<ILoggingEvent> writer = JsonWriter.<ILoggingEvent>of((members) -> {
		members.add("time", ILoggingEvent::getInstant);
		members.add("level", ILoggingEvent::getLevel);
		members.add("thread", ILoggingEvent::getThreadName);
		members.add("message", ILoggingEvent::getFormattedMessage);
		members.add("application").usingMembers((application) -> {
			application.add("name", "StructuredLoggingDemo");
			application.add("version", "1.0.0-SNAPSHOT");
		});
		members.add("node").usingMembers((node) -> {
			node.add("hostname", "node-1");
			node.add("ip", "10.0.0.7");
		});
	}).withNewLineAtEnd();

	@Override
	public String format(ILoggingEvent event) {
		return this.writer.writeToString(event);
	}

}