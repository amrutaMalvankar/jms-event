package com.fedex.jms.eventhub.poc.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.messaging.eventhubs.EventProcessorClient;
import com.fedex.jms.eventhub.poc.processor.EventHubProcessor;
import com.fedex.jms.eventhub.poc.receiver.JmsSender;
import com.fedex.jms.eventhub.poc.util.JmsEventHubConstants;
import com.fedex.jms.eventhub.poc.util.MessageData;

@RestController
public class RESTController {

	private static final Logger log = LoggerFactory.getLogger(RESTController.class);

	@Autowired
	private JmsSender jmsSender;

	@Autowired
	private EventHubProcessor eventHubProcessor;

	private EventProcessorClient eventProcessorClient;

	@RequestMapping(path = "/eventhub/send")
	public ResponseEntity<String> sendMsgToInboundJms() {
		try {
			String message = null;
			List<Map<?, ?>> dataList = MessageData.withQuotesCovertCSVDataToList();
			if (dataList != null && !dataList.isEmpty()) {
				dataList.forEach(msg -> 
				jmsSender.sendMsgToJmsQueue(JmsEventHubConstants.INBOUND_QUEUE_NAME, msg.toString())
				);
				//message = jmsSender.sendMsgToJmsQueue(JmsEventHubConstants.INBOUND_QUEUE_NAME, dataList.toString());
			}
			return new ResponseEntity<String>("Messages are sent in jms queue :: "
					+ JmsEventHubConstants.INBOUND_QUEUE_NAME, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error in sending message!", HttpStatus.EXPECTATION_FAILED);
		}
	}

	@RequestMapping(path = "/eventhub/receive")
	public ResponseEntity<String> receiveMessageFromEventHub() {
		try {
			eventProcessorClient = eventHubProcessor.pullMessageFromEventHub();

			return new ResponseEntity<String>("Event hub consumer started consuming data :: ", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error in sending message!", HttpStatus.EXPECTATION_FAILED);
		}

	}

	@RequestMapping(path = "/eventhub/stop")
	public ResponseEntity<String> stpoEventHub() {
		try {
			eventHubProcessor.stopEventProcessor(eventProcessorClient);

			return new ResponseEntity<String>("Event hub consumer stopped :: ", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error in stopping event hub consumer!", HttpStatus.EXPECTATION_FAILED);
		}

	}
}
